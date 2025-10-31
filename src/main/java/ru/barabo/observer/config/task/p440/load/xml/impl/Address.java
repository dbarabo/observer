package ru.barabo.observer.config.task.p440.load.xml.impl;

import com.thoughtworks.xstream.annotations.XStreamAlias;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@XStreamAlias("АдрПлат")
public final class Address {

	@XStreamAlias("Индекс")
	private String index;

	@XStreamAlias("КодРегион")
	private String region;

	@XStreamAlias("КодРегиона")
	private String regiona;

	@XStreamAlias("Район")
	private String rayon;

	@XStreamAlias("МуниципРайон")
	private String rayonNew;

	@XStreamAlias("Город")
	private String city;

	@XStreamAlias("ГородСелПоселен")
	private String cityNew;

	@XStreamAlias("НаселПункт")
	private String town;

	@XStreamAlias("НаселенПункт")
	private String townNew;

	@XStreamAlias("Улица")
	private String street;

	@XStreamAlias("Дом")
	private String house;

	@XStreamAlias("Корпус")
	private String corpus;

	@XStreamAlias("Квартира")
	private String flat;

	@XStreamAlias("Регион")
	private String regionNew;

	@XStreamAlias("ЭлПланСтруктур")
	private String elPlan;

	@XStreamAlias("ЭлУлДорСети")
	private String elDromNew;

	@XStreamAlias("ЗемелУчасток")
	private String elZemlyNew;

	@XStreamAlias("Здание")
	private String elHouseNew;

	@XStreamAlias("ПомещЗдания")
	private String elHouseNew2;

	@XStreamAlias("ПомещКвартиры")
	private String elFlatNew2;

	public Address() {}


	public static Address createAddress(String region, String index, String rayon,
								String city, String townNew, String street, String house, String corpus, String flat) {

		Address address = new Address();

		address.region = region;
		address.index = index;
		address.rayon = rayon;
		address.city = city;

		//address.townNew = townNew;
        address.town = townNew;

		address.street = street;
		address.house = house;
		address.corpus = corpus;
		address.flat = flat;

		return address;
	}
	
	public static Address parseAddress(String address) {
		if (address == null || "".equals(address.trim())) {
			return null;
		}

		Address adres = new Address();
		
		Pattern pattern = Pattern.compile("index\\{(.*?)\\}");
		Matcher matcher = pattern.matcher(address);

		adres.index = matcher.find() ? matcher.group(1) : null;
		adres.index = adres.index == null || "".equals(adres.index.trim()) ? null : adres.index
				.trim();

		pattern = Pattern.compile("region\\{(.*?)\\}");
		matcher = pattern.matcher(address);
		adres.region = matcher.find() ? matcher.group(1) : null;
		adres.region = adres.region == null || "".equals(adres.region.trim()) ? null : adres.region
				.trim();

		pattern = Pattern.compile("rayon\\{(.*?)\\}");
		matcher = pattern.matcher(address);
		adres.rayon = matcher.find() ? matcher.group(1) : null;
		adres.rayon = adres.rayon == null || "".equals(adres.rayon.trim()) ? null : adres.rayon
				.trim();

		pattern = Pattern.compile("city\\{(.*?)\\}");
		matcher = pattern.matcher(address);
		adres.city = matcher.find() ? matcher.group(1) : null;
		adres.city = adres.city == null || "".equals(adres.city.trim()) ? null : adres.city.trim();

		pattern = Pattern.compile("town\\{(.*?)\\}");
		matcher = pattern.matcher(address);
		adres.town = matcher.find() ? matcher.group(1) : null;
		adres.town = adres.town == null || "".equals(adres.town.trim()) ? null : adres.town.trim();

		pattern = Pattern.compile("street\\{(.*?)\\}");
		matcher = pattern.matcher(address);
		adres.street = matcher.find() ? matcher.group(1) : null;
		adres.street = adres.street == null || "".equals(adres.street.trim()) ? null : adres.street
				.trim();

		pattern = Pattern.compile("house\\{(.*?)\\}");
		matcher = pattern.matcher(address);
		adres.house = matcher.find() ? matcher.group(1) : null;
		adres.house = adres.house == null || "".equals(adres.house.trim()) ? null : adres.house
				.trim();

		pattern = Pattern.compile("corpus\\{(.*?)\\}");
		matcher = pattern.matcher(address);
		adres.corpus = matcher.find() ? matcher.group(1) : null;
		adres.corpus = adres.corpus == null || "".equals(adres.corpus.trim()) ? null : adres.corpus
				.trim();

		pattern = Pattern.compile("flat\\{(.*?)\\}");
		matcher = pattern.matcher(address);
		adres.flat = matcher.find() ? matcher.group(1) : null;
		adres.flat = adres.flat == null || "".equals(adres.flat.trim()) ? null : adres.flat.trim();

		return adres.isNull() ? null : adres;
	}
	
	static public boolean isEmpty(String value) {
		return value == null || "".equals(value.trim());
	}
	
	public boolean isNull() {

		return isEmpty(index) && isEmpty(region) && isEmpty(rayon) && isEmpty(city)
				&& isEmpty(town) && isEmpty(street) && isEmpty(house)
				&& isEmpty(corpus) && isEmpty(flat);
	}
	


	public String getAddress() {
		String addres = String.format("index{%s}", index == null ? "" : index);

		addres += String.format("region{%s}", region == null ? (regionNew == null ? "" : regionNew) : region);

		addres += String.format("rayon{%s}", rayon == null ? (rayonNew == null ? "" : rayonNew) : rayon);

		addres += String.format("city{%s}", city == null ? (cityNew == null ? "" : cityNew) : city);

		addres += String.format("town{%s}", town == null ? (townNew == null ? "" : townNew) : town);

		addres += String.format("street{%s}", street == null ? "" : street);

		addres += String.format("house{%s}", house == null ? "" : house);

		addres += String.format("corpus{%s}", corpus == null ? "" : corpus);

		addres += String.format("flat{%s}", flat == null ? "" : flat);

		return addres;
	}
}
