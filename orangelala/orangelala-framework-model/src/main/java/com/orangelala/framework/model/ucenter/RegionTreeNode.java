package com.orangelala.framework.model.ucenter;

import java.util.List;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class RegionTreeNode extends Region {
	private List<RegionTreeNode> chrildren;
}
