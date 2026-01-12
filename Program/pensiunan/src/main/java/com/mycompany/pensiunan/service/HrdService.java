package com.mycompany.pensiunan.service;

import com.mycompany.pensiunan.dao.HrdDao;
import com.mycompany.pensiunan.dao.PensiunanDao;
import com.mycompany.pensiunan.model.HrdStatistik;
import com.mycompany.pensiunan.model.Pensiunan;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class HrdService {

    private final PensiunanDao pensiunanDao = new PensiunanDao();
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    
    
    private final HrdDao hrdDao = new HrdDao();

    public HrdStatistik getStatistikDashboard() {
        return hrdDao.getStatistik();
    }

    public boolean tambahPensiunan(
            int idHrd,
            String nip,
            String nama,
            String golongan,
            int masaKerja,
            java.time.LocalDate tanggalPensiun,
            String username,
            String passwordPlain
    ) {

        String passwordHash = encoder.encode(passwordPlain);

        Pensiunan p = new Pensiunan(
                nip,
                nama,
                golongan,
                masaKerja,
                tanggalPensiun
        );

        return pensiunanDao.insertPensiunan(
                p,
                username,
                passwordHash,
                idHrd
        );
    }
    
    public boolean hapusPensiunan(String nip) {
        return pensiunanDao.deleteByNip(nip);
    }
}


