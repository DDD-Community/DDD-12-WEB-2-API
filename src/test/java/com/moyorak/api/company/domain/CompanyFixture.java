package com.moyorak.api.company.domain;

import org.springframework.test.util.ReflectionTestUtils;

public class CompanyFixture {

    public static Company fixture(
            final Long id, final double longitude, final double latitude, final boolean use) {
        Company company = new Company();

        ReflectionTestUtils.setField(company, "id", id);
        ReflectionTestUtils.setField(company, "longitude", longitude);
        ReflectionTestUtils.setField(company, "latitude", latitude);
        ReflectionTestUtils.setField(company, "use", use);

        return company;
    }
}
