package com.thorgil.cashbook.core.usecase.company.entity

import com.thorgil.cashbook.core.entity.Company

interface GetCompanyDetails {

    /**
     * Get the company (a business entity) details as they are when this method gets called.
     *
     * May return null because no company details may have been set (initialisation
     * not done).
     *
     * @return
     */
    fun getDetails(): Company?
}