package yazilim.com.yazilim;

public class district {
    int idDistrict;
    int idProvince;
    String district;

    public district(int idDistrict, int idProvince, String district) {
        this.idDistrict = idDistrict;
        this.idProvince = idProvince;
        this.district = district;
    }

    @Override
    public String toString() {
        return district;
    }

    public int getIdDistrict() {
        return idDistrict;
    }

    public int getIdProvince() {
        return idProvince;
    }

    public String getDistrict() {
        return district;
    }
}
