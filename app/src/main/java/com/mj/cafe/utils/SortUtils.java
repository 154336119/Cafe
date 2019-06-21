package com.mj.cafe.utils;

import com.mj.cafe.bean.SeatBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SortUtils {
    public static int getX_MaxNum(List<SeatBean> seatBeanList){
        List<Integer> list1 = new ArrayList<>();
        for(SeatBean seatBean : seatBeanList){
            list1.add(seatBean.getCoordinate_x());
        }
        return Collections.max(list1);
    }

    public static int getY_MaxNum(List<SeatBean> seatBeanList){
        List<Integer> list1 = new ArrayList<>();
        for(SeatBean seatBean : seatBeanList){
            list1.add(seatBean.getCoordinate_y());
        }
        return Collections.max(list1);
    }

    public static List<SeatBean> getTotalList(List<SeatBean> seatBeanList,int maxCol,int maxRow){
        List<SeatBean> totalList = new ArrayList<>();
        for(int y=1;y<maxRow+1;y++){
            List<SeatBean> RowList = new ArrayList<>();
            //遍历所有y行的数据
            List<SeatBean> temRowList = new ArrayList<>();
            for(SeatBean seatBean : seatBeanList){
                if(seatBean.getCoordinate_y() == y){
                    temRowList.add(seatBean);
                }
            }

            //填充y行数据到maxCol长度
            List<SeatBean> temRowColList = new ArrayList<>();
            for(int x = 1;x<maxCol+1;x++){
                boolean hasCol = false;
                for(SeatBean seatBean : temRowList){
                    if(seatBean.getCoordinate_x() == x){
                        temRowColList.add(seatBean);
                        hasCol = true;
                    }
                }
                if(!hasCol){
                    //填充的空数据
                    SeatBean emptySeatBean = new SeatBean();
                    emptySeatBean.setCoordinate_x(x);
                    emptySeatBean.setCoordinate_y(y);
                    emptySeatBean.setEmpty(true);
                    temRowColList.add(emptySeatBean);
                }
            }
            totalList.addAll(temRowColList);
            //添加maxRow个list
        }


        return totalList;
    }
}
