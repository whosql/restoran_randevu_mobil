package yazilim.com.yazilim;

public class province {
    int idProvince;
    String province;

    @Override
    public String toString() {
        return province;
    }

    public province(int idProvince, String province) {
        this.idProvince = idProvince;
        this.province = province;
    }

    public int getIdProvince() {
        return idProvince;
    }

    public String getProvince() {
        return province;
    }
}
