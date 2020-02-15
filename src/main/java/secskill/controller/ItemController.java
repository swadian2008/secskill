package secskill.controller;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import secskill.controller.BaseController;
import secskill.controller.viewmodel.ItemVO;
import secskill.error.BussinessException;
import secskill.error.EmBusinessError;
import secskill.response.CommonReturnType;
import secskill.server.ItemService;
import secskill.server.model.ItemModel;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Administrator
 * @date 2019/11/25/025 20:55
 * @Version 1.0
 */
@RestController
@RequestMapping("/item")
@CrossOrigin(allowCredentials = "true",allowedHeaders = "*") // 跨域请求，防止ajax跨域请求报错标签，值得研究
public class ItemController extends BaseController {

    @Autowired
    private ItemService itemService;

    @RequestMapping(value="/create",method = {RequestMethod.POST},consumes = {CONTENT_TYPE_FORMED})
    public CommonReturnType createItem(@RequestParam(name = "title") String title,
                                       @RequestParam(name = "description")String description,
                                       @RequestParam(name = "price") BigDecimal price,
                                       @RequestParam(name = "stock")Integer stock,
                                       @RequestParam(name = "imgUrl")String imgUrl) throws BussinessException {
        // 封装service请求用来创建商品
        ItemModel itemModel = new ItemModel();
        itemModel.setTitle(title);
        itemModel.setStock(stock);
        itemModel.setDescription(description);
        itemModel.setImgUrl(imgUrl);
        itemModel.setPrice(price);
        ItemModel itemModelReturn = itemService.createItem(itemModel);

        // 模型转出，分模块开发
        ItemVO itemVO = this.convertVOFromModel(itemModelReturn);
        return CommonReturnType.create(itemVO);
    }

    // 商品详情页的浏览
    @RequestMapping(value = "/get",method = {RequestMethod.GET})
    public CommonReturnType getItem(@RequestParam(name = "id") Integer id) throws BussinessException {
        ItemModel itemModel = itemService.getItemById(id);
        ItemVO itemVO = convertVOFromModel(itemModel);
        /*if(6>5){
            throw new BussinessException(EmBusinessError.STOCK_NOT_ENOUGH,"自定义异常！");
        }*/
        return CommonReturnType.create(itemVO);
    }

    // 浏览商品详情列表
    @RequestMapping(value = "/list",method = {RequestMethod.GET})
    public CommonReturnType listItem(){
        List<ItemModel> itemModelList = itemService.listItem();
        List<ItemVO> itemVOList = itemModelList.stream().map(x -> {
            ItemVO itemVO = this.convertVOFromModel(x);
            return itemVO;
        }).collect(Collectors.toList());
        return CommonReturnType.create(itemModelList);
    }


    private ItemVO convertVOFromModel(ItemModel itemModel){
        if(itemModel == null){
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel,itemVO);
        if(itemModel.getPromoModel() != null){
            // 有正在进行的秒杀活动
            itemVO.setPromoStatus(itemModel.getPromoModel().getStatus());
            itemVO.setPromoId(itemModel.getPromoModel().getId());
            itemVO.setStartDate(itemModel.getPromoModel().getStartDate().toString(DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")));
            itemVO.setPromoPrice(itemModel.getPromoModel().getPromoItemPrice());
        }else{
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }
}
