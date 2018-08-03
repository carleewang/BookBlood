package com.base.base.pinyin;


import com.base.util.utility.StringUtil;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class PinyinComparator implements Comparator<LetterSortModel.LetterSortEntity> {

	public int compare(LetterSortModel.LetterSortEntity o1, LetterSortModel.LetterSortEntity o2) {
		if(StringUtil.isStringValid(o1.getLetter()) && StringUtil.isStringValid(o2.getLetter())){
			if (o1.getLetter().equals("@")
					|| o2.getLetter().equals("#")) {
				return -1;
			} else if (o1.getLetter().equals("#")
					|| o2.getLetter().equals("@")) {
				return 1;
			} else {
				return o1.getLetter().compareTo(o2.getLetter());
			}
		}else {
			return -1;
		}

	}

}
