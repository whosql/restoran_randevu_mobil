package yazilim.com.yazilim;

public class personel {
    int idPersonel;
    int idSalon;
    String name;
    String surname;

    public personel(int idPersonel, int idSalon, String name, String surname) {
        this.idPersonel = idPersonel;
        this.idSalon = idSalon;
        this.name = name;
        this.surname = surname;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }

    public int getIdPersonel() {
        return idPersonel;
    }

    public int getIdSalon() {
        return idSalon;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }
}
