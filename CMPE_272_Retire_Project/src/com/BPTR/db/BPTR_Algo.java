package com.BPTR.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.criteria.CriteriaBuilder.In;

import com.BPTR.dto.BPTREvalObject;
import com.BPTR.dto.PlaceInfoObj;
import com.BPTR.dto.Recreation;
import com.BPTR.input.BPTRBean;
import com.google.appengine.api.rdbms.AppEngineDriver;

public class BPTR_Algo {

	Map<Integer, BPTREvalObject> dbDataPersistant = new HashMap<Integer, BPTREvalObject>();

	Map<Integer, List<BPTREvalObject>> finalMap = new HashMap<Integer, List<BPTREvalObject>>();

	public List<PlaceInfoObj> findPlace(BPTRBean cptrDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BPTREvalObject> sudoList = new ArrayList<BPTREvalObject>();
		BPTREvalObject ref = null;

		try {
			con = new DataSource().getConnection();
			String sql = "SELECT LOCATION_ID,LOCATION_NAME,LOCATION_TYPE,SUM(GROCERIES+UTILITIES+OTHER_EXPENSES) AS EXPENSE_1,SUM(GROCERIES+UTILITIES+OTHER_EXPENSES+MEDICAL_EXPENSES) AS EXPENSE_2, "
					+ "APARTMENT_LEASED_MAX,APARTMENT_LEASED_MIN,((APARTMENT_LEASED_MAX+APARTMENT_LEASED_MIN)/2) AS AVG_APARTMENT,"
					+ "HOUSE_OWNED_MAX,HOUSE_OWNED_MIN,((HOUSE_OWNED_MAX+HOUSE_OWNED_MIN)/2) AS HOUSE_AVG FROM LOCATION_MASTER GROUP BY LOCATION_NAME";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				ref = new BPTREvalObject();

				ref.setLocation_id(rs.getString("LOCATION_ID"));
				ref.setLocation_name(rs.getString("LOCATION_NAME"));

				if (rs.getString("LOCATION_TYPE").equalsIgnoreCase("DOWNTOWN")) {
					ref.setLocation(true);
				} else {
					ref.setLocation(false);
				}

				ref.setExpense_1(Integer.parseInt(rs.getString("EXPENSE_1")));
				ref.setExpense_2_wMed(Integer.parseInt(rs
						.getString("EXPENSE_2")));
				ref.setLeased_max(Integer.parseInt(rs
						.getString("APARTMENT_LEASED_MAX")));
				ref.setLeased_min(Integer.parseInt(rs
						.getString("APARTMENT_LEASED_MIN")));
				ref.setLeased_avg(Double.parseDouble(rs
						.getString("AVG_APARTMENT")));
				ref.setOwned_max(Integer.parseInt(rs
						.getString("HOUSE_OWNED_MAX")));
				ref.setOwned_min(Integer.parseInt(rs
						.getString("HOUSE_OWNED_MIN")));
				ref.setOwned_avg(Double.parseDouble(rs.getString("HOUSE_AVG")));
				dbDataPersistant.put(
						Integer.parseInt(rs.getString("LOCATION_ID")), ref);
				sudoList.add(ref);
				System.out.println("THE LOCATION ID ARE ==>"
						+ ref.getLocation_id());
			}
			System.out.println("SUDO LIST SIZE iS ==>" + sudoList.size());
			if (con != null) {
				con.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}

			ref = null;
			int housingExpense;
			int housingExpenseSmall = 0;
			boolean topQualityResidence = false;
			List<BPTREvalObject> tempList = null;
			int counter = 0;
			int mapVal = 0;
			int counterVar = 0;
			// System.out.println(dbDataPersistant.size());
			// System.out.println("Counter size"+counter+"DEstination"+cptrDTO.getNumberOfResults());
			// System.out.println("ounter val "+counterVar);

			int salIncrease = 0;
			String initialComment = "";
			System.out.println("THE NUMBER OF RESULT IS "
					+ cptrDTO.getNumberOfResults());
			System.out.println("THE COUNtER IS " + counter);
			boolean flag=true;
			while (flag) {
								System.out.println("Monthly Income now is ==> "
						+ cptrDTO.getMonthlyIncome());
				System.out.println("The number of location found is ==>"
						+ counter);
				System.out.println("THE OBJECT INFO ITERATATION ==>"
						+ counterVar);
				counterVar++;

				for (int key : dbDataPersistant.keySet()) {
					ref = dbDataPersistant.get(key);

					if (cptrDTO.isLocation()) {

						if (cptrDTO.isHousingPref()) {
							// calculate the monthly expense which would be
							// lease + expense 1
							housingExpense = 0;
							if (cptrDTO.getHousing_type().contains("APT_BASIC")) {
								topQualityResidence = false;
								housingExpense = ref.getLeased_min();
							} else {
								topQualityResidence = true;
								housingExpense = ref.getLeased_max();
								housingExpenseSmall = ref.getLeased_min();
							}
							System.out.println("THE MONTHLY INCOME HERE IS"
									+ cptrDTO.getMonthlyIncome());

							if (!cptrDTO.isMedical_expenses()) {

								// HERE CHECKS WITH THE USERS EXPENSE AND
								// SELECTED APT TYPE EXPENSE
								if (Integer
										.parseInt(cptrDTO.getMonthlyIncome()) >= (housingExpense + ref
										.getExpense_1())) {

									if (ref.isLocation()) {
										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "Best match");
											finalMap.put(1 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment(initialComment
													+ "Best match");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									} else {
										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "This place is located uptown but is a close match to your preference.");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											ref.setComment(initialComment
													+ "This place is located uptown but is a close match to your preference.");
											tempList.add(ref);
											increaseCount(ref);
											counter++;
										}
									}
								}
								// EXPENSE IS MAX SO CAMPARING WITH MINIMUM FOR
								// OUTPU
								else if (topQualityResidence) {
									if (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= (housingExpenseSmall + ref
											.getExpense_1()))

										if (ref.isLocation()) {
											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
												finalMap.put(2 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation and is located uptown but is a close match to your other preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation and is located uptown but is a close match to your other preferences");
												tempList.add(ref);
												increaseCount(ref);
												counter++;
											}
										}

								} else if ((Integer.parseInt(cptrDTO
										.getMonthlyIncome()) * 12 >= ref
										.getExpense_1() + ref.getOwned_max())
										|| (Integer.parseInt(cptrDTO
												.getMonthlyIncome()) * 12) >= ref
												.getExpense_1()
												+ ref.getOwned_min()) {

									if (ref.isLocation()) {
										if (!finalMap.containsKey(5 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself.");
											finalMap.put(5 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(5 + mapVal);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself.");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									} else {
										if (!finalMap.containsKey(6 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself.This place is located uptown");
											finalMap.put(6 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(6 + mapVal);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself.This place is located uptown");
											tempList.add(ref);
											increaseCount(ref);
											counter++;
										}
									}

								}
							} else if (cptrDTO.isMedical_expenses()) {

								// HERE CHECKS WITH THE USERS EXPENSE AND
								// SELECTED APT TYPE EXPENSE
								if (Integer
										.parseInt(cptrDTO.getMonthlyIncome()) >= (housingExpense + ref
										.getExpense_2_wMed())) {

									if (ref.isLocation()) {
										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "Best match");
											finalMap.put(1 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment(initialComment
													+ "Best match");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									} else {
										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "This place is located uptown but is a close match to your preference.");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											ref.setComment(initialComment
													+ "This place is located uptown but is a close match to your preference.");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									}
								}
								// EXPENSE IS MAX SO CAMPARING WITH MINIMUM FOR
								// OUTPU
								else if (topQualityResidence) {
									if (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= (housingExpenseSmall + ref
											.getExpense_2_wMed()))

										if (ref.isLocation()) {
											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
												finalMap.put(2 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation and is located uptown but is a close match to your other preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation and is located uptown but is a close match to your other preferences");
												tempList.add(ref);
												increaseCount(ref);
												counter++;
											}
										}

								} else if ((Integer.parseInt(cptrDTO
										.getMonthlyIncome()) * 12 >= ref
										.getExpense_2_wMed()
										+ ref.getOwned_max())
										|| (Integer.parseInt(cptrDTO
												.getMonthlyIncome()) * 12) >= ref
												.getExpense_2_wMed()
												+ ref.getOwned_min()) {

									if (ref.isLocation()) {
										if (!finalMap.containsKey(5 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself using yoour one years annual income.");
											finalMap.put(5 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(5 + mapVal);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself using yoour one years annual income.");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									} else {
										if (!finalMap.containsKey(6 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself using yoour one years annual income.This place is located uptown ");
											finalMap.put(6 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(6 + mapVal);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself using yoour one years annual income.This place is located uptown");
											tempList.add(ref);
											increaseCount(ref);
											counter++;
										}
									}

								}

							}

						} else if (!cptrDTO.isHousingPref()) {

							if (cptrDTO.getHousing_type().contains(
									"HOUSE_BASIC")) {
								System.out.println("IN IF ELSE 1");
								topQualityResidence = false;
								housingExpense = ref.getOwned_min();
							} else {
								System.out.println("IN IF ELSE 2");
								topQualityResidence = true;
								housingExpense = ref.getOwned_max();
								housingExpenseSmall = ref.getOwned_min();
							}

							if (!cptrDTO.isMedical_expenses()) {
								
								System.out.println("THE VALUE OF HOUSE COST IS"+cptrDTO.getHouseCost());
								System.out.println("The housing expense is"+housingExpense);
								
								if (cptrDTO.getHouseCost() >= housingExpense) {
									if (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= (ref
											.getExpense_1())) {
										if (ref.isLocation()) {
											if (!finalMap
													.containsKey(1 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "Best match");
												finalMap.put(1 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(1 + mapVal);
												ref.setComment(initialComment
														+ "Best match");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(3 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place is located uptown but is a close match to your preference.");
												finalMap.put(3 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(3 + mapVal);
												ref.setComment(initialComment
														+ "This place is located uptown but is a close match to your preference.");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										}
									}

								} else if (topQualityResidence) {
									if (cptrDTO.getHouseCost() >= housingExpenseSmall) {
										if (Integer.parseInt(cptrDTO
												.getMonthlyIncome()) >= (ref
												.getExpense_1())) {
											if (ref.isLocation()) {
												if (!finalMap
														.containsKey(2 + mapVal)) {
													tempList = new ArrayList<BPTREvalObject>();
													tempList.add(ref);
													increaseCount(ref);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
													finalMap.put(2 + mapVal,
															tempList);
													counter++;
												} else {
													tempList = finalMap
															.get(2 + mapVal);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
													tempList.add(ref);
													increaseCount(ref);

													counter++;
												}
											} else {
												if (!finalMap
														.containsKey(4 + mapVal)) {
													tempList = new ArrayList<BPTREvalObject>();
													tempList.add(ref);
													increaseCount(ref);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation and is located uptown but is a close match to your other preferences");
													finalMap.put(4 + mapVal,
															tempList);
													counter++;
												} else {
													tempList = finalMap
															.get(4 + mapVal);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation and is located uptown but is a close match to your other preferences");
													tempList.add(ref);
													increaseCount(ref);
													counter++;
												}
											}

										}

									}
								} 

									if ((Integer.parseInt(cptrDTO.getMonthlyIncome()) >= ref.getExpense_1()+ ref.getLeased_max())
											|| (Integer.parseInt(cptrDTO.getMonthlyIncome())) >= ref.getExpense_1()+ ref.getLeased_min()) 
									{
										if (ref.isLocation()) {
											if (!finalMap
													.containsKey(5 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income.");
												finalMap.put(1 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(5 + mapVal);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income.");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(6 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income..This place is located uptown ");
												finalMap.put(6 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(6 + mapVal);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income..This place is located uptown");
												tempList.add(ref);
												increaseCount(ref);
												counter++;
											}
										}
									}
								
							} else if (cptrDTO.isMedical_expenses()) {

								if (cptrDTO.getHouseCost() >= housingExpense) {
									if (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= (ref
											.getExpense_2_wMed())) {
										if (ref.isLocation()) {
											if (!finalMap
													.containsKey(1 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "Best match");
												finalMap.put(1 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(1 + mapVal);
												ref.setComment(initialComment
														+ "Best match");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(3 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place is located uptown but is a close match to your preference.");
												finalMap.put(3 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(3 + mapVal);
												ref.setComment(initialComment
														+ "This place is located uptown but is a close match to your preference.");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										}
									}

								} else if (topQualityResidence) {
									if (cptrDTO.getHouseCost() >= housingExpenseSmall) {
										if (Integer.parseInt(cptrDTO
												.getMonthlyIncome()) >= (ref
												.getExpense_2_wMed())) {
											if (ref.isLocation()) {
												if (!finalMap
														.containsKey(2 + mapVal)) {
													tempList = new ArrayList<BPTREvalObject>();
													tempList.add(ref);
													increaseCount(ref);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
													finalMap.put(2 + mapVal,
															tempList);
													counter++;
												} else {
													tempList = finalMap
															.get(2 + mapVal);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
													tempList.add(ref);
													increaseCount(ref);

													counter++;
												}
											} else {
												if (!finalMap
														.containsKey(4 + mapVal)) {
													tempList = new ArrayList<BPTREvalObject>();
													tempList.add(ref);
													increaseCount(ref);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation and is located uptown but is a close match to your other preferences");
													finalMap.put(4 + mapVal,
															tempList);
													counter++;
												} else {
													tempList = finalMap
															.get(4 + mapVal);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation and is located uptown but is a close match to your other preferences");
													tempList.add(ref);
													increaseCount(ref);
													counter++;
												}
											}

										}

									}
								} 

									if ((Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= ref
											.getExpense_2_wMed()
											+ ref.getLeased_max())
											|| (Integer.parseInt(cptrDTO
													.getMonthlyIncome())) >= ref
													.getExpense_2_wMed()
													+ ref.getLeased_min()) {
										if (ref.isLocation()) {
											if (!finalMap
													.containsKey(5 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income.");
												finalMap.put(5 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(5 + mapVal);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income.");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(6 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income..This place is located uptown ");
												finalMap.put(6 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(6 + mapVal);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income..This place is located uptown");
												tempList.add(ref);
												increaseCount(ref);
												counter++;
											}
										}
									}
								

							}

						}
					} else if (!cptrDTO.isLocation()) {

						if (cptrDTO.isHousingPref()) {
							// calculate the monthly expense which would be
							// lease + expense 1
							housingExpense = 0;
							if (cptrDTO.getHousing_type().contains("APT_BASIC")) {
								topQualityResidence = false;
								housingExpense = ref.getLeased_min();
							} else {
								topQualityResidence = true;
								housingExpense = ref.getLeased_max();
								housingExpenseSmall = ref.getLeased_min();
							}

							if (!cptrDTO.isMedical_expenses()) {

								// HERE CHECKS WITH THE USERS EXPENSE AND
								// SELECTED APT TYPE EXPENSE
								if (Integer
										.parseInt(cptrDTO.getMonthlyIncome()) >= (housingExpense + ref
										.getExpense_1())) {

									if (!ref.isLocation()) {
										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "Best match");
											finalMap.put(1 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment(initialComment
													+ "Best match");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									} else {
										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "This place is located downtown but is a close match to your preference.");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											ref.setComment(initialComment
													+ "This place is located downtown but is a close match to your preference.");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									}
								}
								// EXPENSE IS MAX SO CAMPARING WITH MINIMUM FOR
								// OUTPU
								else if (topQualityResidence) {
									if (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= (housingExpenseSmall + ref
											.getExpense_1()))

										if (!ref.isLocation()) {
											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
												finalMap.put(2 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation and is located downtown but is a close match to your other preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation and is located downtown but is a close match to your other preferences");
												tempList.add(ref);
												increaseCount(ref);
												counter++;
											}
										}

								} else if ((Integer.parseInt(cptrDTO
										.getMonthlyIncome()) * 12 >= ref
										.getExpense_1() + ref.getOwned_max())
										|| (Integer.parseInt(cptrDTO
												.getMonthlyIncome()) * 12) >= ref
												.getExpense_1()
												+ ref.getOwned_min()) {

									if (!ref.isLocation()) {
										if (!finalMap.containsKey(5 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself.");
											finalMap.put(5 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(5 + mapVal);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself.");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									} else {
										if (!finalMap.containsKey(6 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself.This place is located downtown");
											finalMap.put(6 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(6 + mapVal);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself.This place is located downtown");
											tempList.add(ref);
											increaseCount(ref);
											counter++;
										}
									}

								}
							} else if (cptrDTO.isMedical_expenses()) {

								// HERE CHECKS WITH THE USERS EXPENSE AND
								// SELECTED APT TYPE EXPENSE
								if (Integer
										.parseInt(cptrDTO.getMonthlyIncome()) >= (housingExpense + ref
										.getExpense_2_wMed())) {

									if (!ref.isLocation()) {
										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "Best match");
											finalMap.put(1 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment(initialComment
													+ "Best match");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									} else {
										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "This place is located downtown but is a close match to your preference.");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											ref.setComment(initialComment
													+ "This place is located downtown but is a close match to your preference.");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									}
								}
								// EXPENSE IS MAX SO CAMPARING WITH MINIMUM FOR
								// OUTPU
								else if (topQualityResidence) {
									if (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= (housingExpenseSmall + ref
											.getExpense_2_wMed()))

										if (!ref.isLocation()) {
											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
												finalMap.put(2 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation and is located downtown but is a close match to your other preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide you a smaller accomodation and is located downtown but is a close match to your other preferences");
												tempList.add(ref);
												increaseCount(ref);
												counter++;
											}
										}

								} else if ((Integer.parseInt(cptrDTO
										.getMonthlyIncome()) * 12 >= ref
										.getExpense_2_wMed()
										+ ref.getOwned_max())
										|| (Integer.parseInt(cptrDTO
												.getMonthlyIncome()) * 12) >= ref
												.getExpense_2_wMed()
												+ ref.getOwned_min()) {

									if (!ref.isLocation()) {
										if (!finalMap.containsKey(5 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself using yoour one years annual income.");
											finalMap.put(5 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(5 + mapVal);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself using yoour one years annual income.");
											tempList.add(ref);
											increaseCount(ref);

											counter++;
										}
									} else {
										if (!finalMap.containsKey(6 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											increaseCount(ref);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself using yoour one years annual income.This place is located downtown ");
											finalMap.put(6 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(6 + mapVal);
											ref.setComment(initialComment
													+ "At this location you can buy a permanent house for yourself using yoour one years annual income.This place is located downtown");
											tempList.add(ref);
											increaseCount(ref);
											counter++;
										}
									}

								}

							}

						} else if (!cptrDTO.isHousingPref()) {

							if (cptrDTO.getHousing_type().contains(
									"HOUSE_BASIC")) {
								topQualityResidence = false;
								housingExpense = ref.getOwned_min();
							} else {
								topQualityResidence = true;
								housingExpense = ref.getOwned_max();
								housingExpenseSmall = ref.getOwned_min();
							}

							if (!cptrDTO.isMedical_expenses()) {
								if (cptrDTO.getHouseCost() >= housingExpense) {
									if (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= (ref
											.getExpense_1())) {
										if (!ref.isLocation()) {
											if (!finalMap
													.containsKey(1 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "Best match");
												finalMap.put(1 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(1 + mapVal);
												ref.setComment(initialComment
														+ "Best match");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(3 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place is located downtown but is a close match to your preference.");
												finalMap.put(3 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(3 + mapVal);
												ref.setComment(initialComment
														+ "This place is located downtown but is a close match to your preference.");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										}
									}

								} else if (topQualityResidence) {
									if (cptrDTO.getHouseCost() >= housingExpenseSmall) {
										if (Integer.parseInt(cptrDTO
												.getMonthlyIncome()) >= (ref
												.getExpense_1())) {
											if (!ref.isLocation()) {
												if (!finalMap
														.containsKey(2 + mapVal)) {
													tempList = new ArrayList<BPTREvalObject>();
													tempList.add(ref);
													increaseCount(ref);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
													finalMap.put(2 + mapVal,
															tempList);
													counter++;
												} else {
													tempList = finalMap
															.get(2 + mapVal);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
													tempList.add(ref);
													increaseCount(ref);

													counter++;
												}
											} else {
												if (!finalMap
														.containsKey(4 + mapVal)) {
													tempList = new ArrayList<BPTREvalObject>();
													tempList.add(ref);
													increaseCount(ref);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation and is located downtown but is a close match to your other preferences");
													finalMap.put(4 + mapVal,
															tempList);
													counter++;
												} else {
													tempList = finalMap
															.get(4 + mapVal);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation and is located downtown but is a close match to your other preferences");
													tempList.add(ref);
													increaseCount(ref);
													counter++;
												}
											}

										}

									}
								} 

									if ((Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= ref
											.getExpense_1()
											+ ref.getLeased_max())
											|| (Integer.parseInt(cptrDTO
													.getMonthlyIncome())) >= ref
													.getExpense_1()
													+ ref.getLeased_min()) {
										if (!ref.isLocation()) {
											if (!finalMap
													.containsKey(5 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income.");
												finalMap.put(5 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(5 + mapVal);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income.");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(6 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income..This place is located downtown ");
												finalMap.put(6 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(6 + mapVal);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income..This place is located downtown");
												tempList.add(ref);
												increaseCount(ref);
												counter++;
											}
										}
									}
								
							} else if (cptrDTO.isMedical_expenses()) {

								if (cptrDTO.getHouseCost() >= housingExpense) {
									if (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= (ref
											.getExpense_2_wMed())) {
										if (!ref.isLocation()) {
											if (!finalMap
													.containsKey(1 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "Best match");
												finalMap.put(1 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(1 + mapVal);
												ref.setComment(initialComment
														+ "Best match");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(3 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "This place is located downtown but is a close match to your preference.");
												finalMap.put(3 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(3 + mapVal);
												ref.setComment(initialComment
														+ "This place is located downtown but is a close match to your preference.");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										}
									}

								} else if (topQualityResidence) {
									if (cptrDTO.getHouseCost() >= housingExpenseSmall) {
										if (Integer.parseInt(cptrDTO
												.getMonthlyIncome()) >= (ref
												.getExpense_2_wMed())) {
											if (!ref.isLocation()) {
												if (!finalMap
														.containsKey(2 + mapVal)) {
													tempList = new ArrayList<BPTREvalObject>();
													tempList.add(ref);
													increaseCount(ref);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
													finalMap.put(2 + mapVal,
															tempList);
													counter++;
												} else {
													tempList = finalMap
															.get(2 + mapVal);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation but is a close match to your other preferences");
													tempList.add(ref);
													increaseCount(ref);

													counter++;
												}
											} else {
												if (!finalMap
														.containsKey(4 + mapVal)) {
													tempList = new ArrayList<BPTREvalObject>();
													tempList.add(ref);
													increaseCount(ref);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation and is located downtown but is a close match to your other preferences");
													finalMap.put(4 + mapVal,
															tempList);
													counter++;
												} else {
													tempList = finalMap
															.get(4 + mapVal);
													ref.setComment(initialComment
															+ "This place will provide you a smaller accomodation and is located downtown but is a close match to your other preferences");
													tempList.add(ref);
													increaseCount(ref);
													counter++;
												}
											}

										}

									}
								} 

									if ((Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= ref
											.getExpense_2_wMed()
											+ ref.getLeased_max())
											|| (Integer.parseInt(cptrDTO
													.getMonthlyIncome())) >= ref
													.getExpense_2_wMed()
													+ ref.getLeased_min()) {
										if (!ref.isLocation()) {
											if (!finalMap
													.containsKey(5 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income.");
												finalMap.put(5 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(5 + mapVal);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income.");
												tempList.add(ref);
												increaseCount(ref);

												counter++;
											}
										} else {
											if (!finalMap
													.containsKey(6 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												increaseCount(ref);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income..This place is located downtown ");
												finalMap.put(6 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(6 + mapVal);
												ref.setComment(initialComment
														+ "At this location you can rent a house for yourself in your monthly income..This place is located downtown");
												tempList.add(ref);
												increaseCount(ref);
												counter++;
											}
										}
									}
								

							}

						}

					}

				}

				cptrDTO.setMonthlyIncome(Integer.parseInt(cptrDTO
						.getMonthlyIncome()) + 500 + "");
				salIncrease += 500;
				initialComment = null;
				initialComment = new String();
//				initialComment = "The monthly expense at this place is "
//						+ salIncrease + " more.";
				mapVal += 6;
				if(counterVar >= 10 ||cptrDTO.getNumberOfResults() >= counter ){
					flag=false;
				}

			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		// finalMap=new HashMap<Integer, List<BPTREvalObject>>();
		// finalMap.put(1,sudoList);

		ArrayList<PlaceInfoObj> reference = (ArrayList<PlaceInfoObj>) prepareOutput(finalMap);

		if (reference.size() > cptrDTO.getNumberOfResults()) {
			reference = new ArrayList<PlaceInfoObj>(reference.subList(0,
					cptrDTO.getNumberOfResults()));
		}
		return reference;
	}

	public List<PlaceInfoObj> findPlace1(BPTRBean cptrDTO) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<BPTREvalObject> sudoList = new ArrayList<BPTREvalObject>();
		BPTREvalObject ref = null;

		try {
			con = new DataSource().getConnection();
			String sql = "SELECT LOCATION_ID,LOCATION_NAME,LOCATION_TYPE,SUM(GROCERIES+UTILITIES+OTHER_EXPENSES) AS EXPENSE_1,SUM(GROCERIES+UTILITIES+OTHER_EXPENSES+MEDICAL_EXPENSES) AS EXPENSE_2, "
					+ "APARTMENT_LEASED_MAX,APARTMENT_LEASED_MIN,((APARTMENT_LEASED_MAX+APARTMENT_LEASED_MIN)/2) AS AVG_APARTMENT,"
					+ "HOUSE_OWNED_MAX,HOUSE_OWNED_MIN,((HOUSE_OWNED_MAX+HOUSE_OWNED_MIN)/2) AS HOUSE_AVG FROM LOCATION_MASTER GROUP BY LOCATION_NAME";
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				ref = new BPTREvalObject();

				ref.setLocation_id(rs.getString("LOCATION_ID"));
				ref.setLocation_name(rs.getString("LOCATION_NAME"));

				if (rs.getString("LOCATION_TYPE").equalsIgnoreCase("DOWNTOWN")) {
					ref.setLocation(true);
				} else {
					ref.setLocation(false);
				}

				ref.setExpense_1(Integer.parseInt(rs.getString("EXPENSE_1")));
				ref.setExpense_2_wMed(Integer.parseInt(rs
						.getString("EXPENSE_2")));
				ref.setLeased_max(Integer.parseInt(rs
						.getString("APARTMENT_LEASED_MAX")));
				ref.setLeased_min(Integer.parseInt(rs
						.getString("APARTMENT_LEASED_MIN")));
				ref.setLeased_avg(Double.parseDouble(rs
						.getString("AVG_APARTMENT")));
				ref.setOwned_max(Integer.parseInt(rs
						.getString("HOUSE_OWNED_MAX")));
				ref.setOwned_min(Integer.parseInt(rs
						.getString("HOUSE_OWNED_MIN")));
				ref.setOwned_avg(Double.parseDouble(rs.getString("HOUSE_AVG")));
				dbDataPersistant.put(
						Integer.parseInt(rs.getString("LOCATION_ID")), ref);
				sudoList.add(ref);
				System.out.println("THE LOCATION ID ARE ==>"
						+ ref.getLocation_id());
			}
			System.out.println("SUDO LIST SIZE iS ==>" + sudoList.size());
			if (con != null) {
				con.close();
			}
			if (rs != null) {
				rs.close();
			}
			if (ps != null) {
				ps.close();
			}

			ref = null;
			int housingExpense;
			boolean topQualityResidence = false;
			List<BPTREvalObject> tempList = null;
			int counter = 0;
			int mapVal = 0;
			int counterVar = 0;
			// System.out.println(dbDataPersistant.size());
			// System.out.println("Counter size"+counter+"DEstination"+cptrDTO.getNumberOfResults());
			// System.out.println("ounter val "+counterVar);

			int salIncrease = 0;
			String initialComment = "";
			// while((cptrDTO.getNumberOfResults()>counter)|| counterVar>=10)
			{
				System.out.println("Monthly Income now is ==> "
						+ cptrDTO.getMonthlyIncome());
				System.out.println("The number of location found is ==>"
						+ counter);
				System.out.println("THE OBJECT INFO ITERATATION ==>"
						+ counterVar);
				counterVar++;

				for (int key : dbDataPersistant.keySet()) {

					ref = dbDataPersistant.get(key);
					if (cptrDTO.isHousingPref()) {
						// Calculating the housing expense for the user
						System.out.println("USER LIKE TO LIVE IN DOWNTOWN");

						if (cptrDTO.getHousing_type().contains("APT_BASIC")) {
							housingExpense = ref.getLeased_min();

						} else {
							topQualityResidence = true;
							housingExpense = ref.getLeased_max();
						}

						if (cptrDTO.isMedical_expenses()) {

							if (Integer.parseInt(cptrDTO.getMonthlyIncome()) >= ref
									.getExpense_2_wMed() + housingExpense) {
								if (cptrDTO.isLocation()) {
									if (ref.isLocation()) {
										// ADD 1st Order
										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment("Best match");
											finalMap.put(1 + mapVal, tempList);

											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment("Best match");
											tempList.add(ref);

											counter++;
										}

									} else {
										// ADD object 2nd ORDER PRIORITY
										if (!finalMap.containsKey(3 + mapVal)) {

											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Uptown but is a close match to your preferences");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											ref.setComment(initialComment
													+ "This place is located Uptown but is a close match to your preferences");
											tempList.add(ref);
											counter++;
										}

									}
								} else if (!cptrDTO.isLocation()) {
									if (!ref.isLocation()) {
										// ADD OBJECT TO 1st ORDER LIST

										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(1 + mapVal, tempList);
											ref.setComment("Best match");
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											tempList.add(ref);
											ref.setComment("Best match");
											counter++;
										}

									} else {
										// ADD OBJECT TO 4th ORDER LIST

										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Downtown but is a close match to your preferences");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Downtown but is a close match to your preferences");
											counter++;
										}
									}
								}

							} else if (topQualityResidence) {
								if (Integer
										.parseInt(cptrDTO.getMonthlyIncome()) >= (ref
										.getExpense_2_wMed() + ref
										.getLeased_min())) {
									// ADD THIS WITH SPECFICATION THAT RESIDENCE
									// IS
									// OF LOWER QUALITY

									if (cptrDTO.isLocation()) {
										if (ref.isLocation()) {

											// ADD OBJECT 2nd ORDER LIST

											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												finalMap.put(2 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												tempList.add(ref);
												counter++;
											}

										} else {
											// ADD OBJECT 3d ORDER PRIORITY
											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												finalMap.put(4 + mapVal,
														tempList);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you and is located Uptown but is a close match to your preferences");
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you and is located Uptown but is a close match to your preferences");
												tempList.add(ref);
												counter++;
											}

										}
									} else if (!cptrDTO.isLocation()) {
										if (!ref.isLocation()) {
											// ADD OBJECT TO 2nd ORDER LIST
											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												finalMap.put(2 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											}

										} else {
											// ADD OBJECT TO 3rd ORDER LIST
											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you and is located Downtown but is a close match to your preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you and is located Downtown but is a close match to your preferences");
												counter++;
											}
										}
									}

								}
							}
						} else if (!cptrDTO.isMedical_expenses()) {

							// System.out.println("THE USER HAS INCOME ==> "+cptrDTO.getMonthlyIncome());
							// System.out.println("THE STATE HAS MONTHLY EXPENSE ==> "+(ref.getExpense_1()
							// + housingExpense));

							System.out
									.println("THE USER HAD NO MEDICAL EXPENSE");

							if (Integer.parseInt(cptrDTO.getMonthlyIncome()) >= ref
									.getExpense_1() + housingExpense) {

								if (cptrDTO.isLocation()) {
									if (ref.isLocation()) {
										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(1 + mapVal, tempList);
											ref.setComment("Best match");
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment("Best match");
											tempList.add(ref);
											counter++;
										}

									} else {
										// ADD OBJECT 4th ORDER PRIORITY
										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Uptown but is a close match to your preferences");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Uptown but is a close match to your preferences");
											counter++;
										}

									}
								} else if (!cptrDTO.isLocation()) {
									if (!ref.isLocation()) {
										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(1 + mapVal, tempList);
											ref.setComment("Best match");
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											tempList.add(ref);
											ref.setComment("Best match");
											counter++;
										}
									} else {
										// ADD OBJECT TO 4th ORDER LIST

										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Downtown but is a close match to your preferences");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Downtown but is a close match to your preferences");
											counter++;
										}
									}
								}

							} else if (topQualityResidence) {
								if (Integer
										.parseInt(cptrDTO.getMonthlyIncome()) >= (ref
										.getExpense_1() + ref.getLeased_min())) {
									// ADD THIS WITH SPECFICATION THAT RESIDENCE
									// IS
									// OF LOWER QUALITY

									if (cptrDTO.isLocation()) {
										if (ref.isLocation()) {
											// ADD OBJECT 2nd ORDER LIST

											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												finalMap.put(2 + mapVal,
														tempList);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											}

										} else {
											// ADD OBJECT 3d ORDER PRIORITY

											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you and is located Uptown but is a close match to your preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you and is located Uptown but is a close match to your preferences");
												counter++;
											}

										}
									} else if (!cptrDTO.isLocation()) {
										if (!ref.isLocation()) {
											// ADD OBJECT TO 2nd ORDER LIST

											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												finalMap.put(2 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											}

										} else {
											// ADD OBJECT TO 3rd ORDER LIST

											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you and is located Downtown but is a close match to your preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you and is located Downtown but is a close match to your preferences");
												counter++;
											}
										}
									}

								}
							}

						}
						// HERE SUGGESTION FOR BUYIIN HOUSES

						if ((ref.getOwned_min() >= Integer.parseInt(cptrDTO
								.getMonthlyIncome()) * 12)
								&& (Integer
										.parseInt(cptrDTO.getMonthlyIncome()) * 12 >= ref
										.getOwned_max())) {
							if (cptrDTO.isLocation()) {
								if (ref.isLocation()) {
									// ADD 1st Order
									if (!finalMap.containsKey(5 + mapVal)) {
										tempList = new ArrayList<BPTREvalObject>();
										tempList.add(ref);
										ref.setComment(initialComment
												+ "You can buy a house here using your one years income ");
										finalMap.put(5 + mapVal, tempList);
										counter++;
									} else {
										tempList = finalMap.get(5 + mapVal);
										tempList.add(ref);
										ref.setComment(initialComment
												+ "You can buy a house here using your one years income ");
										counter++;
									}

								} else {
									// ADD object 2nd ORDER PRIORITY
									if (!finalMap.containsKey(6 + mapVal)) {
										tempList = new ArrayList<BPTREvalObject>();
										tempList.add(ref);
										ref.setComment(initialComment
												+ "You can buy a house here using your one years income however this place is located uptown");
										finalMap.put(6 + mapVal, tempList);
										counter++;
									} else {
										tempList = finalMap.get(6 + mapVal);
										tempList.add(ref);
										ref.setComment(initialComment
												+ "You can buy a house here using your one years income however this place is located uptown");
										counter++;
									}

								}
							} else if (!cptrDTO.isLocation()) {
								if (!ref.isLocation()) {
									// ADD OBJECT TO 1st ORDER LIST

									if (!finalMap.containsKey(5 + mapVal)) {
										tempList = new ArrayList<BPTREvalObject>();
										tempList.add(ref);
										ref.setComment(initialComment
												+ "You can buy a house here using your one years income ");
										finalMap.put(5 + mapVal, tempList);
										counter++;
									} else {
										tempList = finalMap.get(5 + mapVal);
										tempList.add(ref);
										ref.setComment(initialComment
												+ "You can buy a house here using your one years income ");
										counter++;

									}

								} else {
									// ADD OBJECT TO 4th ORDER LIST

									if (!finalMap.containsKey(6 + mapVal)) {
										tempList = new ArrayList<BPTREvalObject>();
										tempList.add(ref);
										ref.setComment(initialComment
												+ "You can buy a house here using your one years income however this place is located Downtown");
										finalMap.put(6 + mapVal, tempList);
										counter++;
									} else {
										tempList = finalMap.get(6 + mapVal);
										tempList.add(ref);
										ref.setComment(initialComment
												+ "You can buy a house here using your one years income however this place is located Downtown");
										counter++;
									}
								}
							}
						}

					} else if (!cptrDTO.isHousingPref()) {
						// Calculating the housing expense for the user

						if (cptrDTO.getHousing_type().contains("HOUSE_BASIC")) {
							housingExpense = ref.getOwned_min();
						} else {
							topQualityResidence = true;
							housingExpense = ref.getOwned_max();
						}

						if (cptrDTO.isMedical_expenses()) {

							if (Integer.parseInt(cptrDTO.getMonthlyIncome())
									+ cptrDTO.getHouseCost() >= ref
									.getExpense_2_wMed() + housingExpense) {
								if (cptrDTO.isLocation()) {
									if (ref.isLocation()) {
										// ADD OBJECT 1st ORDER LIST

										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment("Best match");
											finalMap.put(1 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment("Best match");
											tempList.add(ref);
											counter++;
										}

									} else {
										// ADD OBJECT 4th ORDER PRIORITY
										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(3 + mapVal, tempList);
											ref.setComment(initialComment
													+ "This place is located Uptown but is a close match to your preferences");
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Uptown but is a close match to your preferences");
											counter++;
										}

									}
								} else if (!cptrDTO.isLocation()) {
									if (!ref.isLocation()) {
										// ADD OBJECT TO 1st ORDER LIST

										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(1 + mapVal, tempList);
											ref.setComment("Best match");
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment("Best match");
											tempList.add(ref);
											counter++;
										}

									} else {
										// ADD OBJECT TO 4th ORDER LIST
										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Downtown but is a close match to your preferences");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Downtown but is a close match to your preferences");
											counter++;
										}
									}
								}

							} else if (topQualityResidence) {
								if (Integer
										.parseInt(cptrDTO.getMonthlyIncome())
										+ cptrDTO.getHouseCost() >= (ref
										.getExpense_2_wMed() + ref
										.getLeased_min())) {
									// ADD THIS WITH SPECFICATION THAT RESIDENCE
									// IS
									// OF LOWER QUALITY

									if (cptrDTO.isLocation()) {
										if (ref.isLocation()) {
											// ADD OBJECT 2nd ORDER LIST
											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												finalMap.put(2 + mapVal,
														tempList);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											}

										} else {
											// ADD OBJECT 3d ORDER PRIORITY
											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation and is located Uptown for you but is a close match to your preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation and is located Uptown for you but is a close match to your preferences");
												counter++;
											}

										}
									} else if (!cptrDTO.isLocation()) {
										if (!ref.isLocation()) {
											// ADD OBJECT TO 2nd ORDER LIST
											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												finalMap.put(2 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											}
										} else {
											// ADD OBJECT TO 3rd ORDER LIST
											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation and is located Downtown for you but is a close match to your preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation and is located Downtown for you but is a close match to your preferences");
												counter++;
											}
										}
									}

								}
							}
						} else if (!cptrDTO.isMedical_expenses()) {

							if (Integer.parseInt(cptrDTO.getMonthlyIncome()) >= ref
									.getExpense_1() + housingExpense) {
								if (cptrDTO.isLocation()) {
									if (ref.isLocation()) {
										// ADD OBJECT 1st ORDER LIST
										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(1 + mapVal, tempList);
											ref.setComment("Best match");
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment("Best match");
											tempList.add(ref);
											counter++;
										}

									} else {
										// ADD OBJECT 4th ORDER PRIORITY

										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(3 + mapVal, tempList);
											ref.setComment(initialComment
													+ "This place is located Uptown but is a close match to your preferences");
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Uptown but is a close match to your preferences");
											counter++;
										}

									}
								} else if (!cptrDTO.isLocation()) {
									if (!ref.isLocation()) {
										// ADD OBJECT TO 1st ORDER LIST

										if (!finalMap.containsKey(1 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(1 + mapVal, tempList);
											ref.setComment("Best match");
											counter++;
										} else {
											tempList = finalMap.get(1 + mapVal);
											ref.setComment("Best match");
											tempList.add(ref);
											counter++;
										}

									} else {
										// ADD OBJECT TO 4th ORDER LIST

										if (!finalMap.containsKey(3 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Downtown but is a close match to your preferences");
											finalMap.put(3 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(3 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place is located Downtown but is a close match to your preferences");
											counter++;
										}

									}
								}

							} else if (topQualityResidence) {
								if (Integer
										.parseInt(cptrDTO.getMonthlyIncome())
										+ cptrDTO.getHouseCost() >= (ref
										.getExpense_1() + ref.getLeased_min())) {
									// ADD THIS WITH SPECFICATION THAT RESIDENCE
									// IS
									// OF LOWER QUALITY

									if (cptrDTO.isLocation()) {
										if (ref.isLocation()) {
											// ADD OBJECT 2nd ORDER LIST

											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												finalMap.put(2 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											}

										} else {
											// ADD OBJECT 3d ORDER PRIORITY
											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation and is located Uptown but is a close match to your preferences");
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation and is located Uptown but is a close match to your preferences");
												counter++;
											}

										}
									} else if (!cptrDTO.isLocation()) {
										if (!ref.isLocation()) {
											// ADD OBJECT TO 2nd ORDER LIST

											if (!finalMap
													.containsKey(2 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												finalMap.put(2 + mapVal,
														tempList);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											} else {
												tempList = finalMap
														.get(2 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation for you but is a close match to your preferences");
												counter++;
											}

										} else {
											// ADD OBJECT TO 3rd ORDER LIST

											if (!finalMap
													.containsKey(4 + mapVal)) {
												tempList = new ArrayList<BPTREvalObject>();
												tempList.add(ref);
												finalMap.put(4 + mapVal,
														tempList);
												counter++;
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation and is located Downtown but is a close match to your preferences");
											} else {
												tempList = finalMap
														.get(4 + mapVal);
												tempList.add(ref);
												ref.setComment(initialComment
														+ "This place will provide a smaller accomodation and is located Downtown but is a close match to your preferences");
												counter++;
											}
										}
									}

								}
							}

						}

						if (cptrDTO.isMedical_expenses()) {

							if ((ref.getExpense_2_wMed() + ref.getLeased_min() >= Integer
									.parseInt(cptrDTO.getMonthlyIncome()))
									&& (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= ref
											.getExpense_2_wMed()
											+ ref.getLeased_max())) {
								if (cptrDTO.isLocation()) {
									if (ref.isLocation()) {
										// ADD 1st Order
										if (!finalMap.containsKey(5 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house with the monthly income you have");
											finalMap.put(5 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(5 + mapVal);
											ref.setComment(initialComment
													+ "This place will provide you a rented house with the monthly income you have");
											tempList.add(ref);
											counter++;
										}

									} else {
										// ADD object 2nd ORDER PRIORITY
										if (!finalMap.containsKey(6 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house Uptown with the monthly income you have");
											finalMap.put(6 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(6 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house Uptown with the monthly income you have");
											counter++;
										}

									}
								} else if (!cptrDTO.isLocation()) {
									if (!ref.isLocation()) {
										// ADD OBJECT TO 1st ORDER LIST

										if (!finalMap.containsKey(5 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(5 + mapVal, tempList);
											ref.setComment(initialComment
													+ "This place will provide you a rented house with the monthly income you have");
											counter++;
										} else {
											tempList = finalMap.get(5 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house with the monthly income you have");
											counter++;
										}

									} else {
										// ADD OBJECT TO 4th ORDER LIST

										if (!finalMap.containsKey(6 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house Downtown with the monthly income you have");
											finalMap.put(6 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(6 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house Downtown with the monthly income you have");
											counter++;
										}
									}
								}
							}
						} else if (!cptrDTO.isMedical_expenses()) {
							if ((ref.getExpense_1() + ref.getLeased_min() >= Integer
									.parseInt(cptrDTO.getMonthlyIncome()))
									&& (Integer.parseInt(cptrDTO
											.getMonthlyIncome()) >= ref
											.getExpense_1()
											+ ref.getLeased_max())) {
								if (cptrDTO.isLocation()) {
									if (ref.isLocation()) {
										// ADD 1st Order
										if (!finalMap.containsKey(5 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house with the monthly income you have");
											finalMap.put(5 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(5 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house with the monthly income you have");
											counter++;
										}

									} else {
										// ADD object 2nd ORDER PRIORITY
										if (!finalMap.containsKey(6 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house Uptown with the monthly income you have");
											finalMap.put(6 + mapVal, tempList);
											counter++;
										} else {
											tempList = finalMap.get(6 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house Uptown with the monthly income you have");
											counter++;
										}

									}
								} else if (!cptrDTO.isLocation()) {
									if (!ref.isLocation()) {

										// ADD OBJECT TO 1st ORDER LIST

										if (!finalMap.containsKey(5 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(5 + mapVal, tempList);
											ref.setComment(initialComment
													+ "This place will provide you a rented house with the monthly income you have");
											counter++;
										} else {
											tempList = finalMap.get(5 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house with the monthly income you have");
											counter++;
										}

									} else {

										// ADD OBJECT TO 4th ORDER LIST

										if (finalMap.containsKey(6 + mapVal)) {
											tempList = new ArrayList<BPTREvalObject>();
											tempList.add(ref);
											finalMap.put(6 + mapVal, tempList);
											ref.setComment(initialComment
													+ "This place will provide you a rented house Downtown with the monthly income you have");
											counter++;
										} else {
											tempList = finalMap.get(6 + mapVal);
											tempList.add(ref);
											ref.setComment(initialComment
													+ "This place will provide you a rented house Downtown with the monthly income you have");
											counter++;
										}
									}
								}
							}
						}
					}
				}

				cptrDTO.setMonthlyIncome(Integer.parseInt(cptrDTO
						.getMonthlyIncome()) + 500 + "");
				salIncrease += 500;
				initialComment = "The monthly expense at this place is "
						+ salIncrease + " more.";
				mapVal += 7;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		// finalMap=new HashMap<Integer, List<BPTREvalObject>>();
		// finalMap.put(1,sudoList);
		return prepareOutput(finalMap);
	}

	boolean checkObject(List<BPTREvalObject> finalItrList, BPTREvalObject comp) {
		boolean result = true;
		for (BPTREvalObject ref : finalItrList) {
			if (comp.getLocation_id() == ref.getLocation_id()) {
				result = false;
				break;
			}
		}
		return result;
	}

	ArrayList<PlaceInfoObj> prepareOutput(
			Map<Integer, List<BPTREvalObject>> locMap) {

		Set<String> location_id = new HashSet<String>();
		List<BPTREvalObject> finalItrList = new ArrayList<BPTREvalObject>();

		// HERE GETTING OBJECT FROM MAP AND UNIQUES LOCATION

		int maxPriority = 0;
		for (int key : locMap.keySet()) {
			System.out.println("THE KEY IS ==>" + key);
			if (key > maxPriority) {
				maxPriority = key;
			}
		}
		System.out.println("VALUE OF MAX PRIORITY IS" + maxPriority);

		for (int priority = 1; maxPriority >= priority; priority++) {

			System.out.println(priority);
			if (locMap.containsKey(priority)) {
				System.out.println("THE KEY VALUE IS ==>" + priority);

				for (BPTREvalObject itr : locMap.get(priority)) {
					location_id.add(itr.getLocation_id());
					System.out.println("THE LOCATION IS ==> "
							+ itr.getLocation_name());
					System.out.println("THE COMMENT IS ==>" + itr.getComment());

					if (checkObject(finalItrList, itr)) {
						finalItrList.add(itr);
					}
				}
			}

		}

		// CREATING SINGLE STRING OF LOCATION_ID
		String location_list = "";
		for (String loc : location_id) {
			location_list = location_list + loc + ",";
		}

		if (location_list.contains(","))
			location_list = location_list.substring(0,
					location_list.length() - 1);
		System.out.println("Loaction_LISt" + location_list);
		// RETRIEVED INFO BASED ON LOCATION_ID

		List<PlaceInfoObj> finalSorting = null;
		if (location_list != "" || (location_list.length() > 0)
				|| location_list != null) {
			finalSorting = retrieveInformation(location_list);
		}
		// SORTING ACCORDING TO PRIORITY

		Map<String, PlaceInfoObj> sortingMap = new HashMap<String, PlaceInfoObj>();
		ArrayList<PlaceInfoObj> returnList = new ArrayList<PlaceInfoObj>();

		for (PlaceInfoObj ref : finalSorting) {
			sortingMap.put(ref.getLocation_id(), ref);
		}

		PlaceInfoObj info = null;
		for (BPTREvalObject ref : finalItrList) {

			info = sortingMap.get(ref.getLocation_id());
			info.setComment(ref.getComment());
			info.setMonthlyExpwM("" + ref.getExpense_2_wMed());
			info.setMonthlyExp("" + ref.getExpense_1());
			returnList.add(info);
		}
		return returnList;
	}

	List<PlaceInfoObj> retrieveInformation(String locationId) {
		List<PlaceInfoObj> finalDisplayList = new ArrayList<PlaceInfoObj>();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			con = new DataSource().getConnection();

			String sql = "SELECT * FROM LOCATION_MASTER WHERE LOCATION_ID in ( "
					+ locationId + " )";
			System.out.println("THE SQL IS " + sql);
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			PlaceInfoObj dto = null;
			while (rs.next()) {
				dto = new PlaceInfoObj();
				dto.setCountry_id(rs.getString("COUNTRY_ID"));
				dto.setaMax(rs.getString("APARTMENT_LEASED_MAX"));
				dto.setaMin(rs.getString("APARTMENT_LEASED_MIN"));
				dto.setLocation_id(rs.getString("LOCATION_ID"));
				dto.setLocationName(rs.getString("LOCATION_NAME"));
				dto.setLat(rs.getString("LATITUDE"));
				dto.setLang(rs.getString("LONGITUDE"));
				dto.sethMax(rs.getString("HOUSE_OWNED_MAX"));
				dto.sethMin(rs.getString("HOUSE_OWNED_MIN"));
				dto.setGroceries(rs.getString("GROCERIES"));
				dto.setUtilities(rs.getString("UTILITIES"));
				dto.setOtherExpenses(rs.getString("OTHER_EXPENSES"));
				dto.setMedicalExpenses(rs.getString("MEDICAL_EXPENSES"));
				dto.setLocationType(rs.getString("LOCATION_TYPE"));
				dto.setClimate(rs.getString("CLIMATE"));
				dto.setInfo(getInfo(dto.getLocation_id()));
				dto.setImages(getPicture(dto.getLocation_id()));
				dto.setRecreationInfo(getRecreation(dto.getLocation_id()));
				finalDisplayList.add(dto);
			}

			System.out.println("FINAL DISPLAY SIZE" + finalDisplayList.size());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return finalDisplayList;
	}

	List<String> getPicture(String locationId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<String> info = new ArrayList<String>();
		try {
			String sql = "SELECT IMAGE_PATH FROM LOCATION_IMAGES WHERE LOCATION_ID = "
					+ locationId;
			con = new DataSource().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				info.add(rs.getString("IMAGE_PATH"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return info;
	}

	String getInfo(String locationId) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		String info = "";
		try {
			String sql = "SELECT DESCRIPTION FROM LOCATION_DESC WHERE LOCATION_ID = "
					+ locationId;
			con = new DataSource().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				info = info + rs.getString("DESCRIPTION");
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return info;
	}

	List<Recreation> getRecreation(String location_id) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		Recreation ref = null;
		List<Recreation> info = new ArrayList<Recreation>();
		try {
			String sql = "SELECT * FROM LOCATION_RECREATION WHERE LOCATION_ID = "
					+ location_id;
			con = new DataSource().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			while (rs.next()) {
				ref = new Recreation();
				ref.setType(rs.getString("RECREATION_TYPE"));
				ref.setName(rs.getString("RECREATION_NAME"));
				ref.setLat(rs.getString("LATITUDE"));
				ref.setLang(rs.getString("LONGITUDE"));
				info.add(ref);
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
		return info;

	}

	void increaseCount(BPTREvalObject ref) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		System.out.println("REACHED HERE IN UPDATE TREND");
		// List<Recreation> info = new ArrayList<Recreation>();
		try {
			String sql = "SELECT * FROM TREND_MASTER WHERE LOCATION_ID = "
					+ ref.getLocation_id();
			con = new DataSource().getConnection();
			ps = con.prepareStatement(sql);
			rs = ps.executeQuery();
			System.out.println(" THE RESULT SET SIZE IS " + rs);
			if (rs.next()) {
				int count = 1;
				count = rs.getInt("SEARCH_COUNT");
				System.out.println("THE COUNT IS ==>" + count);

				if (rs != null)
					rs.close();

				if (ps != null)
					ps.close();

				sql = "UPDATE TREND_MASTER SET SEARCH_COUNT= " + (count + 1)
						+ " WHERE LOCATION_ID = " + ref.getLocation_id();
				ps = con.prepareStatement(sql);
				ps.executeUpdate();
			} else {
				System.out.println("IN the ELSE PART OF CODE");
				if (rs != null)
					rs.close();

				if (ps != null)
					ps.close();

				sql = "SELECT * FROM LOCATION_MASTER WHERE LOCATION_ID in ( "
						+ ref.getLocation_id() + " )";
				System.out.println("THE SQL IS " + sql);
				ps = con.prepareStatement(sql);
				rs = ps.executeQuery();
				PlaceInfoObj dto = null;
				while (rs.next()) {
					dto = new PlaceInfoObj();
					dto.setCountry_id(rs.getString("COUNTRY_ID"));
					dto.setaMax(rs.getString("APARTMENT_LEASED_MAX"));
					dto.setaMin(rs.getString("APARTMENT_LEASED_MIN"));
					dto.setLocation_id(rs.getString("LOCATION_ID"));
					dto.setLocationName(rs.getString("LOCATION_NAME"));
					dto.setLat(rs.getString("LATITUDE"));
					dto.setLang(rs.getString("LONGITUDE"));
					dto.sethMax(rs.getString("HOUSE_OWNED_MAX"));
					dto.sethMin(rs.getString("HOUSE_OWNED_MIN"));
					dto.setGroceries(rs.getString("GROCERIES"));
					dto.setUtilities(rs.getString("UTILITIES"));
					dto.setOtherExpenses(rs.getString("OTHER_EXPENSES"));
					dto.setMedicalExpenses(rs.getString("MEDICAL_EXPENSES"));
					dto.setLocationType(rs.getString("LOCATION_TYPE"));
					dto.setClimate(rs.getString("CLIMATE"));
					dto.setInfo(getInfo(dto.getLocation_id()));
					dto.setImages(getPicture(dto.getLocation_id()));
					dto.setRecreationInfo(getRecreation(dto.getLocation_id()));
				}

				if (rs != null)
					rs.close();

				if (ps != null)
					ps.close();

				String statement = "INSERT INTO TREND_MASTER(LOCATION_ID,LOCATION_NAME,APARTMENT_LEASED_MAX,APARTMENT_LEASED_MIN,HOUSE_OWNED_MAX,HOUSE_OWNED_MIN,GROCERIES,UTILITIES,OTHER_EXPENSES,MEDICAL_EXPENSES,LOCATION_TYPE,CLIMATE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
				ps = con.prepareStatement(statement);
				ps.setString(1, dto.getLocation_id());
				ps.setString(2, dto.getLocationName());
				ps.setString(3, dto.getaMax());
				ps.setString(4, dto.getaMin());
				ps.setString(5, dto.gethMax());
				ps.setString(6, dto.gethMin());
				ps.setString(7, dto.getGroceries());
				ps.setString(8, dto.getUtilities());
				ps.setString(9, dto.getOtherExpenses());
				ps.setString(10, dto.getMedicalExpenses());
				ps.setString(11, dto.getLocationType());
				ps.setString(12, dto.getClimate());
				ps.execute();
			}
		} catch (Exception e) {
			e.printStackTrace();
			// TODO: handle exception
		} finally {

			if (con != null) {
				try {
					con.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (ps != null) {
				try {
					ps.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (rs != null) {
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

	}

	public static void main(String[] args) throws Exception {

	}
}
