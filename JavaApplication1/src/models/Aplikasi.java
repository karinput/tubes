/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package models;

/**
 *
 * @author JOKER
 */
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Aplikasi {
    private Collection<Mahasiswa> listMahasiswa;
    private Collection<Pembimbing> listPembimbing;
    private Collection<LokasiGladi> listLokasiGladi;
    private Collection<Kelompok> listKelompok;
    private int idUniqueGenerator;
    private int menuChoice;
    private int menuLevel;
    private Orang loggedInUser;
    
    public Aplikasi() {
        this.listMahasiswa = new ArrayList<>();
        this.listPembimbing = new ArrayList<>();
        this.listLokasiGladi = new ArrayList<>();
        this.listKelompok = new ArrayList<>();
        this.idUniqueGenerator = 0;
        this.menuChoice = 0;
        this.menuLevel = 0;
        this.loggedInUser = null;
    }
    
    private int generateId() {
        this.idUniqueGenerator += 1;
        
        return this.idUniqueGenerator;
    }
    
    private void resetMenu() {
        this.menuChoice = 0;
        this.menuLevel = 0;
    }
    
    private void loginMenuDisplay() {
        System.out.println("masukkan id number: ");
    }
    
    private void greetMenuDisplay() {
        System.out.println("1. Login via id");
        System.out.println("2. register mahasiswa");
        System.out.println("3. register pembimbing");
        System.out.println("pilih nomor menu: ");

    }
    
    private void mahasiswaMenuDisplay() {
        System.out.println("1. lihat info geladi");
        System.out.println("2. pilih lokasi geladi");
        System.out.println("3. logout");
        System.out.println("pilih nomor menu: ");

    }
    
    private void pembimbingMenuDisplay() {
        System.out.println("1. list lokasi geladi");
        System.out.println("2. add lokasi geladi");
        System.out.println("3. edit data pembimbing");
        System.out.println("4. logout");
        System.out.println("pilih nomor menu: ");

    }
    
    private Collection<Kelompok> getKelompokByGeladi(int geladiId) {
        return this.listKelompok.stream()
                .filter(k -> k.getIdLokasiGladi() == geladiId)
                .collect(Collectors.toList());
    }
    
    private void listInfoGeladi() {
        if (this.listLokasiGladi.size() < 1) {
            System.out.println("Tidak ada lokasi geladi saat ini");
        } else {
            this.listLokasiGladi.forEach(g -> {
                System.out.println("lokasi: "+g.getKota());
                System.out.println("kuota: "+g.getKuota());
                System.out.println("Nama :"+ g.getNama());
                System.out.println("No ID: "+g.getId());
            });
        }
    }
    
    private Orang findOrangByNoId(String noId) throws NoSuchElementException {
        return Stream.concat(this.listMahasiswa.stream(), this.listPembimbing.stream())
                .filter(o -> o.getNoId().equalsIgnoreCase(noId))
                .findFirst()
                .get();
    }
    
    private Mahasiswa findMahasiswaByNoId(String noId) {
        return this.listMahasiswa.stream()
                .filter(m -> m.getNoId().equalsIgnoreCase(noId))
                .findFirst()
                .get();
    }
    
    private Collection<Mahasiswa> findMahasiswaByKelompokId(int kelId) {
        return this.listMahasiswa.stream()
                .filter(m -> m.getIdKelompok() == kelId)
                .collect(Collectors.toList());
    }
    
    private Collection<Mahasiswa> findMahasiswaByGladiId(int gladiId){
        return this.listMahasiswa.stream()
                .filter(m -> m.getLokasi() == gladiId)
                .collect(Collectors.toList());
    }
    private void addGeladiToMahasiswa(String nim, int id) {
        this.listMahasiswa = this.listMahasiswa.stream().map((Mahasiswa m) -> {
            if (m.getNoId().equalsIgnoreCase(nim)){
                m.setLokasi(id);
            }
            
            return m;
        }).collect(Collectors.toList());
    }
    
    private void addKelompokToMahasiswa(String nim, int id) {
       this.listMahasiswa = this.listMahasiswa.stream().map((Mahasiswa m) -> {
            if (m.getNoId().equalsIgnoreCase(nim)){
                m.setIdKelompok(id);
            }
            
            return m;
        }).collect(Collectors.toList());
    }
    
    private void removeKelompokFromMahasiswa(String nim, int id) {
        this.listMahasiswa = this.listMahasiswa.stream().map((Mahasiswa m) -> {
            if (m.getNoId().equalsIgnoreCase(nim)){
                m.setIdKelompok(0);
            }
            
            return m;
        }).collect(Collectors.toList());
    }
    
    private void loginHandler(String noId) {
        try {
            this.loggedInUser = this.findOrangByNoId(noId);
        } catch (NoSuchElementException e) {
            System.out.println("id tidak terdaftar");
        }
    }
    
    private LokasiGladi getGeladi(int id) {
       return this.listLokasiGladi.stream()
                .filter(g -> g.getId() == id)
                .findFirst()
                .get();
    }
    
    private void logout() {
        this.loggedInUser = null;
    }
    
    private void updateInfoGladi(int idGladi, String nama, String lokasi) {
        this.listLokasiGladi = this.listLokasiGladi.stream()
                .map((LokasiGladi m) -> {
                    if (m.getId() == idGladi) {
                        m.setKota(lokasi);
                        m.setNama(nama);
                    }
                    
                    return m;
                }).collect(Collectors.toList());
    }
    
    private void addKuotaGladi(int id, int n){
        this.listLokasiGladi = this.listLokasiGladi.stream()
                .map((LokasiGladi g) -> {
                    if (g.getId() == id) {
                        g.setKuota(g.getKuota() + n);
                    }
                    
                    return g;
                }).collect(Collectors.toList());
    }
    
    private void decrementKuotaGladi(LokasiGladi g, int n) {
        int howMany = this.findMahasiswaByGladiId(g.getId()).size();
        int result = g.getKuota() - n;
        
        if (result < howMany) {
            System.out.println("kuota baru tidak baluh kurang dari banyak mahasiswa");
        } else {
            this.listLokasiGladi = this.listLokasiGladi.stream()
                .map((LokasiGladi glad) -> {
                    if (glad.getId() == g.getId()) {
                        glad.setKuota(result);
                    }
                    
                    return glad;
                }).collect(Collectors.toList());
        }
    }
    
    private void addKelompok(int gladiId){
        this.listKelompok.add(new Kelompok(gladiId, this.generateId()));
    }
    
    private void removeGladi(int gladiId) {
        this.listLokasiGladi.removeIf(g -> g.getId() == gladiId);
        
        Collection<Kelompok> kelompok = this.getKelompokByGeladi(gladiId);
        kelompok.forEach(k -> {
            this.listKelompok.removeIf(kel -> kel.getIdKelompok() == k.getIdKelompok());
            this.listMahasiswa = this.listMahasiswa.stream()
                    .map((Mahasiswa m) -> {
                        if (m.getIdKelompok() == k.getIdKelompok()) {
                            m.setIdKelompok(0);
                            m.setLokasi(gladiId);
                        }
                        
                        return m;
                    }).collect(Collectors.toList());
        });
    }
    
    private void addGeladi(String nama, String lokasi, int kuota) {
        LokasiGladi newLokasi = new LokasiGladi(lokasi, this.generateId(), kuota, nama);
        this.listLokasiGladi.add(newLokasi);
    }
    
    private void updatePembimbing(String id, String nama, String nohp) {
        this.listPembimbing = this.listPembimbing.stream()
                .map((Pembimbing p) -> {
                    if (p.getNoId().equalsIgnoreCase(id)) {
                        p.setNama(nama);
                        p.setNohp(nohp);
                    }
                    
                    return p;
                }).collect(Collectors.toList());
    }
    
    public void mainMenu() {
        while (true) {
            Scanner reader = new Scanner(System.in);
            if (this.loggedInUser == null) {
                this.greetMenuDisplay();
                try {
                    switch(reader.nextInt()) {
                        case 1:
                            this.loginMenuDisplay();
                            this.loginHandler(reader.next());
                            break;
                        case 2:
                            System.out.println("Masukkan nama mahasiswa: ");
                            String nama = reader.next();
                            System.out.println("Masukkan id mahasiswa: ");
                            String noId = reader.next();
                            System.out.println("Masukkan nohp: ");
                            String nohp = reader.next();
                            Mahasiswa m = new Mahasiswa(nama, noId, nohp);
                            this.listMahasiswa.add(m);
                            break;
                        case 3:
                            System.out.println("Masukkan nama pembimbing: ");
                            String namaP = reader.next();
                            System.out.println("Masukkan id pembimbing: ");
                            String noIdP = reader.next();
                            System.out.println("Masukkan nohp: ");
                            String nohpP = reader.next();
                            Pembimbing p = new Pembimbing(namaP, noIdP, nohpP);
                            this.listPembimbing.add(p);
                            break;
                        default:
                            System.out.println("menu tidak ada");
                            break;
                    }
                } catch (Exception e) {
                    System.out.println("whoopss... something's not right");
                }
            } else if (this.loggedInUser.getJabatan().equalsIgnoreCase("mahasiswa")) {
                    this.mahasiswaMenuDisplay();
                    try {
                        switch(reader.nextInt()) {
                            case 1:
                                this.listInfoGeladi();
                                break;
                            case 2:
                                if (this.findMahasiswaByNoId(this.loggedInUser.getNoId()).getLokasi() < 1) {
                                    System.out.println("masukkan id geladi: ");
                                    this.addGeladiToMahasiswa(this.loggedInUser.getNoId(), reader.nextInt());
                                    System.out.println("geladi sudah masuk");
                                } else {
                                    LokasiGladi g = this.getGeladi(this.findMahasiswaByNoId(this.loggedInUser.getNoId()).getLokasi());
                                    System.out.println("info geladi yang anda masukkan");
                                    System.out.println("nama: "+g.getNama());
                                    System.out.println("lokasi: "+g.getKota());
                                }
                                break;
                            case 3:
                                this.logout();
                                break;
                            default:
                                System.out.println("menu not found");
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("whoooppsss.... something's not right");
                    }
                } else if (this.loggedInUser.getJabatan().equalsIgnoreCase("pembimbing")) {
                    this.pembimbingMenuDisplay();
                    try {
                        switch (reader.nextInt()) {
                            case 1:
                                this.listInfoGeladi();
                                System.out.println("pilih id lokasi geladi untuk lihat detail: ");
                                this.geladiSubmenu(reader, this.getGeladi(reader.nextInt()));
                                break;
                            case 2:
                                System.out.println("input nama perusahaan: ");
                                String per = reader.next();
                                System.out.println("input lokasi: ");
                                String lok = reader.next();
                                System.out.println("input kuota: ");
                                int kuota = reader.nextInt();
                                this.addGeladi(per, lok, kuota);
                                break;
                            case 3:
                                System.out.println("masukkan nama baru:("+this.loggedInUser.getNama()+")");
                                String namaBar = reader.next();
                                System.out.println("masukkan nohp baru:("+this.loggedInUser.getNohp()+")");
                                String noHp = reader.next();
                                this.updatePembimbing(this.loggedInUser.getNoId(), namaBar, noHp);
                                System.out.println("info updated....");
                                break;
                            case 4:
                                this.logout();
                                break;
                            default:
                                System.out.println("menu not found");
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("ada yang gabener nih vroh");
                    }
                }
            }
        }
    
    private void geladiSubmenu(Scanner reader, LokasiGladi gladi) {
        System.out.println("nama: "+gladi.getNama());
        System.out.println("kota: "+gladi.getKota());
        System.out.println(
                        "kuota: " 
                        + gladi.getKuota() 
                        + " sisa: " 
                        + (gladi.getKuota() - this.findMahasiswaByGladiId(gladi.getId()).size())
        );
        this.getKelompokByGeladi(gladi.getId()).forEach(k -> {
            System.out.println("id Kelompok: " + k.getIdKelompok());
            System.out.println("daftar mahasiswa: ");
            this.findMahasiswaByKelompokId(gladi.getId()).forEach(m -> {
                System.out.println("nama: "+m.getNama());
                System.out.println("id mahasiswa: "+m.getNoId());
                System.out.println("nohp: "+m.getNohp());
            });
        });
        System.out.println();
        System.out.println("1. edit info geladi");
        System.out.println("2. tambah kuota geladi");
        System.out.println("3. kurangi kuota geladi");
        System.out.println("4. tambah kelompok");
        System.out.println("5. edit kelompok");
        System.out.println("6. remove lokasi geladi");
        switch (reader.nextInt()) {
            case 1:
                System.out.println("masukkan nama baru:("+gladi.getNama()+")");
                String namaBar = reader.next();
                System.out.println("masukkan lokasi bary:("+gladi.getKota()+")");
                String lokasiBar = reader.next();
                this.updateInfoGladi(gladi.getId(), namaBar, lokasiBar);
                System.out.println("lokasi updated....");
                break;
            case 5:
                System.out.println("1. tambah mahasiswa");
                System.out.println("2. kurangi mahasiswa");
                int menu = reader.nextInt();
                if (menu ==1) {
                    System.out.println("masukkan id mahasiswa");
                    String mId = reader.next();
                    System.out.println("masukkan id kelompok");
                    int kId = reader.nextInt();
                    this.addKelompokToMahasiswa(mId, kId);
                    System.out.println("mahasiswa berhasil dimasukkan");
                } else if (menu == 2){
                    System.out.println("masukkan id mahasiswa");
                    String mId2 = reader.next();
                    System.out.println("masukkan id kelompok");
                    int kId2 = reader.nextInt();
                    this.removeKelompokFromMahasiswa(mId2, kId2);
                    System.out.println("mahasiswa berhasil dihapus dari kelompok");
                }
                break;
            case 2:
                System.out.println("masukkan jumlah yang anda ingin tambah:");
                this.addKuotaGladi(gladi.getId(), reader.nextInt());
                break;
            case 3:
                System.out.println("masukkan jumlah yang anda ingin kurangi:");
                this.decrementKuotaGladi(gladi, reader.nextInt());
                break;
            case 4:
                this.addKelompok(gladi.getId());
                System.out.println("kelompok berhasi dibuat");
                break;
            case 6:
                this.removeGladi(gladi.getId());
                System.out.println("lokasi berhasil dihapus");
                break;
            default:
                System.out.println("menu not found");
                break;
        }
    }
}
    

