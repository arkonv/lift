package ru.smartsarov.lift.test.geocoder;

import java.math.BigDecimal;

public class GeoAddress {
	public String fullAddress;
	public BigDecimal lat;
	public BigDecimal lng;
	
	public GeoAddress(String fullAddress, BigDecimal lat, BigDecimal lng) {
		this.fullAddress = fullAddress;
		this.lat = lat;
		this.lng = lng;
	}
}