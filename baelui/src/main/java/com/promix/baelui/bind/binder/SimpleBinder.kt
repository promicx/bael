package com.promix.baelui.bind.binder

import com.promix.baelui.bind.base.BvBase

class SimpleBinder(variable: Int, layoutId: Int) : ConditionalDataBinder<BvBase<Any>>(variable, layoutId) {
    override fun canHandle(model: BvBase<Any>): Boolean {
        return true
    }
}