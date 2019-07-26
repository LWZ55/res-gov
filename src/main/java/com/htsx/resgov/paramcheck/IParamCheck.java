package com.htsx.resgov.paramcheck;

import com.huatai.xtrade.xstep.event.IXStepEvent;

public interface IParamCheck {

    boolean paramCheck(IXStepEvent xStepEvent) throws Exception;

}
