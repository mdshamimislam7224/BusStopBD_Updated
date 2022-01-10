package com.shamim.newbusstop.Inventory_Spinner_NearBy_Place;

import com.shamim.newbusstop.R;

import java.util.ArrayList;
import java.util.List;

public class Data_NearBy_Place
{
    public static List<NearBy_Place_Class> getNearByPlace()
    {
        List<NearBy_Place_Class> nearbyPlaceList = new ArrayList<>();

        NearBy_Place_Class selectOption= new NearBy_Place_Class();
        selectOption.setName("Choose Option");
        selectOption.setImage(R.drawable.search_ic);
        nearbyPlaceList.add(selectOption);

        NearBy_Place_Class mosque= new NearBy_Place_Class();
        mosque.setName("Mosque");
        mosque.setImage(R.drawable.mosque);
        nearbyPlaceList.add(mosque);

        NearBy_Place_Class hospitalPlace= new NearBy_Place_Class();
        hospitalPlace.setName("Hospital");
        hospitalPlace.setImage(R.drawable.hospital);
        nearbyPlaceList.add(hospitalPlace);


        NearBy_Place_Class atmPlace= new NearBy_Place_Class();
        atmPlace.setName("ATM BOOTH");
        atmPlace.setImage(R.drawable.atm);
        nearbyPlaceList.add(atmPlace);

        NearBy_Place_Class gasPlace= new NearBy_Place_Class();
        gasPlace.setName("Gas Station");
        gasPlace.setImage(R.drawable.gas_station);
        nearbyPlaceList.add(gasPlace);


        NearBy_Place_Class restaurantPlace= new NearBy_Place_Class();
        restaurantPlace.setName("Restaurant");
        restaurantPlace.setImage(R.drawable.ic_baseline_restaurant_24);
        nearbyPlaceList.add(restaurantPlace);


        NearBy_Place_Class schoolPlace= new NearBy_Place_Class();
        schoolPlace.setName("School");
        schoolPlace.setImage(R.drawable.ic_school);
        nearbyPlaceList.add(schoolPlace);

        return nearbyPlaceList;

    }
}
