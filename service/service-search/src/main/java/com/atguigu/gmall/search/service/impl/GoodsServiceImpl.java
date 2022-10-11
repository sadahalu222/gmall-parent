package com.atguigu.gmall.search.service.impl;
import com.atguigu.gmall.model.list.SearchAttr;
import com.atguigu.gmall.model.vo.search.*;
import com.google.common.collect.Lists;

import com.atguigu.gmall.model.list.Goods;
import com.atguigu.gmall.search.repository.GoodsRepository;
import com.atguigu.gmall.search.service.GoodsService;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.NestedQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.nested.ParsedNested;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedLongTerms;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.HighlightQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class GoodsServiceImpl implements GoodsService {
    @Autowired
    GoodsRepository goodsRepository;
    @Autowired
    ElasticsearchRestTemplate esRestTemplate;

    @Override
    public void saveGoods(Goods goods) {
        goodsRepository.save(goods);
    }

    @Override
    public void deleteGoods(Long skuId) {
        goodsRepository.deleteById(skuId);
    }

    @Override
    public SearchResponseVo search(SearchParamVo paramVo) {
        //1.动态构建出搜索条件
        Query query=buildQueryDsl(paramVo);


        //搜索 并得到此次响应结果
        SearchHits<Goods> goods = esRestTemplate.search(query, Goods.class, IndexCoordinates.of("goods"));

        //3.将搜索条件进行转换
        SearchResponseVo responseVo=buildSearchResponseResult(goods,paramVo);
        return responseVo;
    }

    @Override
    public void updateHotScore(Long skuId, Long score) {
        Goods goods = goodsRepository.findById(skuId).get();
        goods.setHotScore(score);
        goodsRepository.save(goods);
    }

    /**
     * 根据检索到达记录 构建响应结果
     * @param goods
     * @return
     */
    private SearchResponseVo buildSearchResponseResult(SearchHits<Goods> goods,SearchParamVo paramVo) {
        SearchResponseVo vo =new SearchResponseVo();

        //1.当时检索前端传过来的参数
        vo.setSearchParam(paramVo);
        //2.品牌面包屑 trademark=4:小米
        if (!StringUtils.isEmpty(paramVo.getTrademark())){
            vo.setTrademarkParam("品牌: "+paramVo.getTrademark());
        }
        //3.平台属性面包屑
        if(paramVo.getProps()!=null &&paramVo.getProps().length>0){

            List<SearchAttr> propsParamList=new ArrayList<>();
            String[] props = paramVo.getProps();
            for (String prop : props) {
                //23:8G:运行内存
                String[] split = prop.split(":");
                //一个 searchAttr 代表一个属性面包屑
                SearchAttr searchAttr = new SearchAttr();
                searchAttr.setAttrValue(split[1]);
                searchAttr.setAttrName(split[2]);
                searchAttr.setAttrId(Long.parseLong(split[0]));
                propsParamList.add(searchAttr);
            }
            vo.setPropsParamList(propsParamList);
        }
        // 4.所有品牌列表 需要es聚合分析
        List<TrademarkVo> trademarkList=buildTrademarkList(goods);
        vo.setTrademarkList(trademarkList);


        //todo 5.所有属性列表 需要es聚合分析
        List<AttrVo> attrsList=buildAttrList(goods);
        vo.setAttrsList(attrsList);

        //6.返回排序信息 order=1:desc
        if(!StringUtils.isEmpty(paramVo.getOrder())){
            String order = paramVo.getOrder();
            OrderMapVo mapVo = new OrderMapVo();
            mapVo.setType(order.split(":")[0]);
            mapVo.setSort(order.split(":")[1]);
            vo.setOrderMap(mapVo);
        }

        //7.所有搜索到的商品列表
        ArrayList<Goods> goodsList = new ArrayList<>();
        List<SearchHit<Goods>> hits = goods.getSearchHits();
        for (SearchHit<Goods> hit : hits) {
            //这条命中记录的商品
            Goods content = hit.getContent();
            //如果模糊检索了 会有高亮
            if(!StringUtils.isEmpty(paramVo.getKeyword())){
                String highlightTitle = hit.getHighlightField("title").get(0);
                content.setTitle(highlightTitle);
            }
            goodsList.add(content);
        }
        vo.setGoodsList(goodsList);

        //8.页码
        vo.setPageNo(paramVo.getPageNo());
        //9.总页码
        long totalHits = goods.getTotalHits();
        long ps=totalHits%8 ==0?totalHits/8:(totalHits/8+1);

        vo.setTotalPages(new Integer(ps+""));

        //10 老链接
        String url = makeUrlParam(paramVo);
        vo.setUrlParam(url);

        return vo;
    }

    /**
     * 分析得到，当前检索的结果中，所有商品涉及了多少种平台属性
     * @param goods
     * @return
     */
    private List<AttrVo> buildAttrList(SearchHits<Goods> goods) {

        List<AttrVo> attrVos  = new ArrayList<>();

        //1、拿到整个属性的聚合结果
        ParsedNested attrAgg = goods.getAggregations().get("attrAgg");


        //2、拿到属性id的聚合结果
        ParsedLongTerms attrIdAgg = attrAgg.getAggregations().get("attrIdAgg");

        //3、遍历所有属性id
        for (Terms.Bucket bucket : attrIdAgg.getBuckets()) {
            AttrVo attrVo = new AttrVo();


            //3.1、属性id
            long attrId = bucket.getKeyAsNumber().longValue();
            attrVo.setAttrId(attrId);

            //3.2、属性名
            ParsedStringTerms attrNameAgg = bucket.getAggregations().get("attrNameAgg");
            String attrName = attrNameAgg.getBuckets().get(0).getKeyAsString();
            attrVo.setAttrName(attrName);
            //3.3、所有属性值
            List<String> attrValues = new ArrayList<>();
            ParsedStringTerms attrValueAgg = bucket.getAggregations().get("attrValueAgg");
            for (Terms.Bucket valueBucket : attrValueAgg.getBuckets()) {
                String value = valueBucket.getKeyAsString();
                attrValues.add(value);
            }
            attrVo.setAttrValueList(attrValues);


            attrVos.add(attrVo);
        }

        return attrVos;
    }


    /**
     * 分析得到 当前检索到结果中 所有商品涉及了多少种品牌
     * @return
     */
    private List<TrademarkVo> buildTrademarkList(SearchHits<Goods> goods) {

        List<TrademarkVo> trademarkVoList = new ArrayList<>();
        // 拿到tmIdAgg 聚合
        ParsedLongTerms tmIdAgg = goods.getAggregations().get("tmIdAgg");

        List<? extends Terms.Bucket> buckets = tmIdAgg.getBuckets();
        //拿到品牌id桶里面的每一个数据
        for (Terms.Bucket bucket : buckets) {
            TrademarkVo trademarkVo = new TrademarkVo();
            //1 获取品牌的id
            long tmId = bucket.getKeyAsNumber().longValue();

            //2.获取品牌名
            ParsedStringTerms tmNameAgg = bucket.getAggregations().get("tmNameAgg");
            String tmName = tmNameAgg.getBuckets().get(0).getKeyAsString();

            //3.获取品牌logo
            ParsedStringTerms tmLogoAgg = bucket.getAggregations().get("tmLogoAgg");
            String tmLogo = tmLogoAgg.getBuckets().get(0).getKeyAsString();

            trademarkVo.setTmId(tmId);
            trademarkVo.setTmName(tmName);
            trademarkVo.setTmLogoUrl(tmLogo);
            trademarkVoList.add(trademarkVo);
        }

        return trademarkVoList;
    }

    /**
     * 制造老链接
     * @param paramVo
     * @return
     */
    private String makeUrlParam(SearchParamVo paramVo) {
        //list.html?&k=v 不非法
        StringBuilder builder = new StringBuilder();
        builder.append("list.html?");
        //1.拼三级分类所有参数
        if(paramVo.getCategory1Id()!=null){
            builder.append("&category1Id="+paramVo.getCategory1Id());
        }
        if(paramVo.getCategory2Id()!=null){
            builder.append("&category2Id="+paramVo.getCategory2Id());
        }
        if(paramVo.getCategory3Id()!=null){
            builder.append("&category3Id="+paramVo.getCategory3Id());
        }

        //2、拼关键字
        if(!StringUtils.isEmpty(paramVo.getKeyword())){
            builder.append("&keyword="+paramVo.getKeyword());
        }

        //3、拼品牌
        if(!StringUtils.isEmpty(paramVo.getTrademark())){
            builder.append("&trademark="+paramVo.getTrademark());
        }

        //4、拼属性
        if(paramVo.getProps()!=null && paramVo.getProps().length >0){
            for (String prop : paramVo.getProps()) {
                //props=23:8G:运行内存
                builder.append("&props="+prop);
            }
        }

//   前端有     //5、拼排序
//        builder.append("&order="+paramVo.getOrder());
//
//        //6、拼页码
//        builder.append("&pageNo="+paramVo.getPageNo());


        //拿到最终字符串
        String url = builder.toString();
        return url;


    }

    /**
     * 根据前端传来的所有请求参数构建检索条件
     * @param paramVo
     * @return
     */
    private Query buildQueryDsl(SearchParamVo paramVo) {
        //1.准备bool
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //2.给bool中准备must的各个条件
        //2.1 前端传了分类
        if (paramVo.getCategory1Id()!=null){

            boolQuery.must( QueryBuilders.termQuery("category1Id", paramVo.getCategory1Id()));
        }
        if (paramVo.getCategory2Id()!=null){

            boolQuery.must( QueryBuilders.termQuery("category2Id", paramVo.getCategory2Id()));
        }
        if (paramVo.getCategory3Id()!=null){

            boolQuery.must( QueryBuilders.termQuery("category3Id", paramVo.getCategory3Id()));
        }

        //2.2 前端穿了 keyword 要进行全文检索
        if(!StringUtils.isEmpty(paramVo.getKeyword())){
            boolQuery.must(QueryBuilders.matchQuery("title", paramVo.getKeyword()));
        }

        //2.3 前端传来品牌 trademark=4:小米
        if(!StringUtils.isEmpty(paramVo.getTrademark())){
            long tmId = Long.parseLong(paramVo.getTrademark().split(":")[0]);
            boolQuery.must(QueryBuilders.termQuery("tmId", tmId));

        }

        //2.4 前端传了属性 props=4:128GB:机身存储&props=5:骁龙730:CPU型号
        String[] props = paramVo.getProps();
        if(props!=null && props.length>0){
            for (String prop : props) {
                //(4:128GB:机身存储)  得到属性和值
                String[] split = prop.split(":");
                long attrId = Long.parseLong(split[0]);
                String attrValue=split[1];
                //构造boolQuery
                BoolQueryBuilder nestedBool = QueryBuilders.boolQuery();
                nestedBool.must(QueryBuilders.termQuery("attrs.attrId", attrId));
                nestedBool.must(QueryBuilders.termQuery("attrs.attrValue", attrValue));

                NestedQueryBuilder nestedQuery = QueryBuilders.nestedQuery("attrs", nestedBool, ScoreMode.None);
                //给最大的boolQuery里面放嵌入式查询
                boolQuery.must(nestedQuery);
            }
        }

        //--------------检索条件结束------------------------

        //0.准备一个原生检索条件[原生的dsl]
        NativeSearchQuery query = new NativeSearchQuery(boolQuery);


        //2.5 前端传了排序 order=2:asc
        if(!StringUtils.isEmpty(paramVo.getOrder())){
            String[] split = paramVo.getOrder().split(":");
            //分析用哪个字段
            String orderField = "hotScore";
            switch (split[0]){
                case "1": orderField = "hotScore"; break;
                case "2": orderField = "price"; break;
                case "3": orderField = "createTime"; break;
                default: orderField = "hotScore";
            }
            Sort sort = Sort.by(orderField);

            if (split[1].equals("asc")){
                sort = sort.ascending();
            }

            if (split[1].equals("desc")){
                sort = sort.descending();
            }
            query.addSort(sort);
        }

        //2.6 前端传了页码
        if(paramVo.getPageNo()!=null&&paramVo.getPageNo()>0){
            //页码在Spring底层从0开始
            PageRequest request = PageRequest.of(paramVo.getPageNo()-1, 8);
            query.setPageable(request);
        }

        //=======================排序分页结束============================

        //2.7 关键字高亮显示
        if(!StringUtils.isEmpty(paramVo.getKeyword())){

            HighlightBuilder highlightBuilder = new HighlightBuilder();

            highlightBuilder.field("title").preTags("<span style='color:red'>").postTags("</span>");

            HighlightQuery highlightQuery = new HighlightQuery(highlightBuilder);

            query.setHighlightQuery(highlightQuery);
        }
        //==========模糊查询高亮功能结束===========
        //3.品牌聚合 - 品牌聚合分析条件

        TermsAggregationBuilder tmIdAgg = AggregationBuilders.terms("tmIdAgg")//自己起的
                .field("tmId").size(1000);

        //3.1 品牌名字聚合
        TermsAggregationBuilder tmNameAgg = AggregationBuilders.terms("tmNameAgg").field("tmName").size(1);
        //3.2 品牌logo聚合
        TermsAggregationBuilder tmLogoAgg= AggregationBuilders.terms("tmLogoAgg").field("tmLogoUrl").size(1);

        tmIdAgg.subAggregation(tmNameAgg);
        tmIdAgg.subAggregation(tmLogoAgg);

        //品牌id聚合条件拼装完成
        query.addAggregation(tmIdAgg );


        //4.属性聚合
        //4.1 属性的整个嵌入式聚合
        NestedAggregationBuilder attrAgg = AggregationBuilders.nested("attrAgg", "attrs");
        //4.2 attrId 聚合
        TermsAggregationBuilder attrIdAgg = AggregationBuilders.terms("attrIdAgg").field("attrs.attrId").size(100);

        //4.3 attrName 聚合
        TermsAggregationBuilder attrNameAgg = AggregationBuilders.terms("attrNameAgg").field("attrs.attrName").size(1);

        //4.4 attrValue 聚合
        TermsAggregationBuilder attrValueAgg = AggregationBuilders.terms("attrValueAgg").field("attrs.attrValue").size(100);

        attrIdAgg.subAggregation(attrNameAgg);
        attrIdAgg.subAggregation(attrValueAgg);

        attrAgg.subAggregation(attrIdAgg);
        // 添加整个属性的聚合条件
        query.addAggregation(attrAgg);

        return query;
    }









}
