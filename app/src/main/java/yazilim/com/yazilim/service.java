package yazilim.com.yazilim;

public class service {
    int idService;
    int idtype;
    String service;

    public service(int idService, int idtype, String service) {
        this.idService = idService;
        this.idtype = idtype;
        this.service = service;
    }

    @Override
    public String toString() {
        return service;
    }

    public int getIdService() {
        return idService;
    }

    public int getIdtype() {
        return idtype;
    }

    public String getService() {
        return service;
    }
}
