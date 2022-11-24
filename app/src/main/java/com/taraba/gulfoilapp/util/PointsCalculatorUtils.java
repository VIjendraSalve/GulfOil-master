package com.taraba.gulfoilapp.util;

import com.taraba.gulfoilapp.model.PointsCalculatorMaster;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by AND707 on 03-Jul-18.
 */
public class PointsCalculatorUtils {
    public List<String> getCategoryList(List<PointsCalculatorMaster> pcmList) {
        List<String> categoryList = new ArrayList<>();

        for (int i = 0; i < pcmList.size(); i++) {


            if (categoryList.size() == 0) {
                categoryList.add(pcmList.get(i).getCategory());
            } else if (!categoryList.contains(pcmList.get(i).getCategory())) {
                categoryList.add(pcmList.get(i).getCategory());
            }

        }
        Collections.sort(categoryList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        categoryList.add(0, "Select Item Category Description");
        return categoryList;
    }

    public List<String> getSegDesList(List<PointsCalculatorMaster> pcmList, String category) {
        List<String> segDesList = new ArrayList<>();

        for (int i = 0; i < pcmList.size(); i++) {

            if (pcmList.get(i).getCategory().equals(category)) {
                if (segDesList.size() == 0) {
                    segDesList.add(pcmList.get(i).getProduct_segment_description());
                } else if (!segDesList.contains(pcmList.get(i).getProduct_segment_description())) {
                    segDesList.add(pcmList.get(i).getProduct_segment_description());
                }
            }
        }
        Collections.sort(segDesList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        segDesList.add(0, "Select Item Description");
        return segDesList;
    }

    public List<String> getBrandDescList(List<PointsCalculatorMaster> pcmList, String category, String segDes) {
        List<String> brandDesList = new ArrayList<>();

        for (int i = 0; i < pcmList.size(); i++) {

            if (pcmList.get(i).getCategory().equals(category)
                    && pcmList.get(i).getProduct_segment_description().equals(segDes)) {
                if (brandDesList.size() == 0) {
                    brandDesList.add(pcmList.get(i).getBrand_description());
                } else if (!brandDesList.contains(pcmList.get(i).getBrand_description())) {
                    brandDesList.add(pcmList.get(i).getBrand_description());
                }
            }
        }
        Collections.sort(brandDesList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        brandDesList.add(0, "Select Product Classification");
        return brandDesList;
    }

    public List<String> getPackSizeList(List<PointsCalculatorMaster> pcmList, String category, String segDes, String brand) {
        List<String> packSizeList = new ArrayList<>();

        for (int i = 0; i < pcmList.size(); i++) {

            if (pcmList.get(i).getCategory().equals(category)
                    && pcmList.get(i).getProduct_segment_description().equals(segDes)
                    && pcmList.get(i).getBrand_description().equals(brand)) {
                if (packSizeList.size() == 0) {
                    packSizeList.add(pcmList.get(i).getPack_size_description());
                } else if (!packSizeList.contains(pcmList.get(i).getPack_size_description())) {
                    packSizeList.add(pcmList.get(i).getPack_size_description());
                }
            }
        }
        Collections.sort(packSizeList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return o1.compareTo(o2);
            }
        });
        packSizeList.add(0, "Select Material Code");
        return packSizeList;
    }
}
