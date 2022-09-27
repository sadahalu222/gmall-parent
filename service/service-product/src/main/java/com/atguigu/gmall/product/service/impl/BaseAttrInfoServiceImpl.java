package com.atguigu.gmall.product.service.impl;

import com.atguigu.gmall.model.product.BaseAttrInfo;
import com.atguigu.gmall.model.product.BaseAttrValue;
import com.atguigu.gmall.product.mapper.BaseAttrValueMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.atguigu.gmall.product.service.BaseAttrInfoService;
import com.atguigu.gmall.product.mapper.BaseAttrInfoMapper;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class BaseAttrInfoServiceImpl extends ServiceImpl<BaseAttrInfoMapper, BaseAttrInfo>
    implements BaseAttrInfoService{

    @Autowired
    BaseAttrInfoMapper baseAttrInfoMapper;
    @Autowired
    BaseAttrValueMapper baseAttrValueMapper;
    @Override
    public List<BaseAttrInfo> getAttrInfoAndValueByCategoryId(Long c1Id, Long c2Id, Long c3Id) {
        List<BaseAttrInfo> infos = baseAttrInfoMapper.getAttrInfoAndValueByCategoryId(c1Id,c2Id,c3Id);
        return infos;
    }

    @Override
    public void saveAttrInfo(BaseAttrInfo info) {
        if(null == info.getId()){
            //进行新增
            addBaseAttrInfo(info);
        }else {
            //进行修改
            updateBaseAttrInfo(info);
        }
    }

    private void updateBaseAttrInfo(BaseAttrInfo info) {

        //修改属性名
        baseAttrInfoMapper.updateById(info);

        //1先把前端删除过的进行删除
        List<BaseAttrValue> valueList = info.getAttrValueList();
        //(1)前端提交过来的所有属性值id
        List<Long> vids = new ArrayList<>();
        for (BaseAttrValue attrValue : valueList) {
            Long id = attrValue.getId();
            if(id != null){
                vids.add(id);
            }
        }
        //(2) delete * from base_attr_value where attr_id=11 and id not in(15,61)
        if (vids.size()>0){
            //部分删除
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id", info.getId());
            deleteWrapper.notIn("id", vids);
            baseAttrValueMapper.delete(deleteWrapper);
        }else {
            //全删除 说明前端一个id都没带 把这个属性id下的所有属性值全删掉
            QueryWrapper<BaseAttrValue> deleteWrapper = new QueryWrapper<>();
            deleteWrapper.eq("attr_id", info.getId());
            baseAttrValueMapper.delete(deleteWrapper);
        }

        //2.修改属性值

        for (BaseAttrValue attrValue : valueList) {
            if (attrValue.getId()!=null){
                //修改
                baseAttrValueMapper.updateById(attrValue);
            }else {
                //新增
                attrValue.setAttrId(info.getId());
                baseAttrValueMapper.insert(attrValue);
            }

        }
    }

    private void addBaseAttrInfo(BaseAttrInfo info) {
        //1.保存属性名
        baseAttrInfoMapper.insert(info);
        //拿到刚才保存好的属性名的自增id
        Long id = info.getId();

        //2.保存属性值
        List<BaseAttrValue> valueList = info.getAttrValueList();
        for (BaseAttrValue value : valueList) {
            //回填属性名记录的自增id(约等于外键)
            value.setAttrId(id);
            baseAttrValueMapper.insert(value);
        }
    }
}




