package com.smchambi.sample;

import com.smchambi.dto.ShippingOrder;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BenchmarchLocal {


	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
				.include(BenchmarchLocal.class.getSimpleName())
				.warmupIterations(0)
				.measurementIterations(3)
				.build();

		new Runner(opt).run();
	}

	@State(Scope.Thread)
	public static class BenchMarkTest {
		public ShippingOrder order;

		@Setup
		public void setup() {
			var parcel1 = new ShippingOrder.ParcelGroup.Parcel("1", "pending");
			var parcel2 = new ShippingOrder.ParcelGroup.Parcel("2", "pending");
			var parcel3 = new ShippingOrder.ParcelGroup.Parcel("3", "pending");
			var parcelGroup = new ShippingOrder.ParcelGroup(List.of(parcel1, parcel2, parcel3));
			var parcelGroups = List.of(parcelGroup);
			order = new ShippingOrder(parcelGroups);
		}

	}

	//Legacy way
	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@BenchmarkMode(Mode.AverageTime)
	public void legacyWay(BenchMarkTest benchMarkTest) {
		getParcelsSameStatus(benchMarkTest.order);
	}

	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@BenchmarkMode(Mode.AverageTime)
	public void currentWay(BenchMarkTest benchMarkTest) {
		getShippingOrderStatus(benchMarkTest.order);
	}

	@Benchmark
	@OutputTimeUnit(TimeUnit.NANOSECONDS)
	@BenchmarkMode(Mode.AverageTime)
	public void legacyProposalWay(BenchMarkTest benchMarkTest) {
		getUniqueParcelStatus(benchMarkTest.order);
	}


	private Optional<String> getParcelsSameStatus(ShippingOrder shippingOrder) {
		return shippingOrder.retrieveAllParcels().stream()
				.map(ShippingOrder.ParcelGroup.Parcel::getStatus)
				//if all statuses are the same return it, otherwise return ""
				.reduce((previous, current) -> previous.equals(current) ? current : "")
				//if status equals "" we return an empty optional, otherwise return the status
				.filter(status -> !status.isEmpty());
	}

	//Current way
	private static Optional<String> getShippingOrderStatus(ShippingOrder shippingOrder) {
		var uniqueValues = shippingOrder.retrieveAllParcels().stream()
				.map(ShippingOrder.ParcelGroup.Parcel::getStatus)
				.distinct()
				.toArray(String[]::new);
		if (uniqueValues.length == 1) {
			return Optional.of(uniqueValues[0]);
		}
		return Optional.empty();
	}

	//Legacy proposal way
	private static Optional<String> getUniqueParcelStatus(ShippingOrder shippingOrder) {
		var uniqueValues = shippingOrder
				.retrieveAllParcels().stream()
				.map(ShippingOrder.ParcelGroup.Parcel::getStatus)
				.collect(Collectors.toList());
		if (uniqueValues.size() == 1) return Optional.of(uniqueValues.get(0));
		return Optional.empty();
	}
}
