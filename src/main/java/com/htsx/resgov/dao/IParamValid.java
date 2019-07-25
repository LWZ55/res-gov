package com.htsx.resgov.dao;

import com.huatai.xtrade.xstep.event.IXStepEvent;

public interface IParamValid {

    boolean paramValid(IXStepEvent xStepEvent) throws Exception;

}
