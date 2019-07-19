package com.htsx.resgov.dao;

import com.huatai.xtrade.xstep.event.IXStepEvent;

public interface IParamValid<T> {
    boolean paramValid(IXStepEvent xStepEvent);

}
