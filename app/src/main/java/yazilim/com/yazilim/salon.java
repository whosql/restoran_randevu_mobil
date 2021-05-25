package yazilim.com.yazilim;

public class salon {
    int idSalon;
    int idSalonType;
    String salon;
    int idProvince;
    int idDistrict;

    public int getIdSalon() {
        return idSalon;
    }

    public int getIdSalonType() {
        return idSalonType;
    }

    public int getIdProvince() {
        return idProvince;
    }

    public int getIdDistrict() {
        return idDistrict;
    }

    public String getSalon() {
        return salon;
    }

    public salon(int idSalon, int idSalonType, String salon,int idProvince, int idDistrict) {
        this.idSalon = idSalon;
        this.idSalonType = idSalonType;
        this.salon = salon;
        this.idProvince = idProvince;
        this.idDistrict = idDistrict;
    }

    @Override
    public String toString() {
        return salon;
    }
}
