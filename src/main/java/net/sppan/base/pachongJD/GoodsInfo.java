package net.sppan.base.pachongJD;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class GoodsInfo {
    private Integer id;

    private String goodsId;

    private String goodsName;

    private String imgUrl;

    private String goodsPrice;
    public GoodsInfo( String goodsId,String goodsName,String imgUrl, String goodsPrice){
        this.goodsId=goodsId;
        this.goodsName=goodsName;
        this.imgUrl=imgUrl;
        this.goodsPrice=goodsPrice;
    }
}
