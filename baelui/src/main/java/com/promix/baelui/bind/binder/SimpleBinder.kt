package com.promix.baelui.bind.binder

import com.promix.baelui.bind.base.VmBase
import com.promix.baelui.bind.binder.core.ConditionalDataBinder

class SimpleBinder(variable: Int, layoutId: Int) : ConditionalDataBinder<VmBase<Any>>(variable, layoutId) {
    override fun canHandle(model: VmBase<Any>): Boolean {
        return true
    }
}