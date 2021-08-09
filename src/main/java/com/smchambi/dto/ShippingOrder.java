package com.smchambi.dto;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class ShippingOrder {

	private final List<ParcelGroup> parcelGroups;

	public ShippingOrder(List<ParcelGroup> parcelGroups) {
		this.parcelGroups = parcelGroups;
	}

	public List<ParcelGroup.Parcel> retrieveAllParcels() {
		return parcelGroups
				.stream()
				.map(ParcelGroup::getParcels)
				.flatMap(Collection::stream)
				.collect(Collectors.toList());
	}

	public static class ParcelGroup {
		private final List<Parcel> parcels;

		public ParcelGroup(List<Parcel> parcels) {
			this.parcels = parcels;
		}

		public List<Parcel> getParcels() {
			return parcels;
		}

		public static class Parcel {
			private final String id;
			private final String status;

			public Parcel(String id, String status) {
				this.id = id;
				this.status = status;
			}

			public String getId() {
				return id;
			}

			public String getStatus() {
				return status;
			}
		}
	}

}
