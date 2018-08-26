/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.gor;

import static com.mycompany.gor.Keyauth.hasKey;
import static com.mycompany.gor.Keyauth.keywords;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author MTaufiqAkbar
 */
@ManagedBean(name = "Collab")
@RequestScoped
public class Collab {

    private final String url = "jdbc:postgresql://localhost:5432/dblipi";
    private final String user = "postgres";
    private final String password = "mtaufiqakbar";
    static String Id, Au, Af, Sb, ArrIdSaatIni, TempArrIdSaatIni, NewArrIdSaatIni, target, value, source;
    static boolean statSameJurnal;
    static int Idx, SourceIdx, TargetIdx, ValueIdx, TempLastIdx, IdxArrRel;
    static ArrayList<String> ArrId = new ArrayList<String>();
    static ArrayList<String> ArrAu = new ArrayList<String>();
    static ArrayList<String> ArrAf = new ArrayList<String>();
    static ArrayList<String> ArrSb = new ArrayList<String>();
    static ArrayList<String> ArrRel = new ArrayList<String>();
    static ArrayList<Integer> ArrRelVal = new ArrayList<Integer>();
    static ArrayList<String> ArrRelId = new ArrayList<String>();
    static ArrayList<String> ArrRelSub = new ArrayList<String>();
    static ArrayList<String> ArrRelAff = new ArrayList<String>();
    static ArrayList<String> ArrDat = new ArrayList<String>();
    static ArrayList<String> ArrDaf = new ArrayList<String>();
    static ArrayList<Integer> ArrVal = new ArrayList<Integer>();
    static ArrayList<Integer> JValue = new ArrayList<Integer>();
    static ArrayList<String> JName = new ArrayList<String>();
    static ArrayList<String> JSubject = new ArrayList<String>();
    static ArrayList<String> JAff = new ArrayList<String>();
    static ArrayList<Integer> ArrDatVal = new ArrayList<Integer>();
    static ArrayList<String> ArrRel1 = new ArrayList<String>();
    static ArrayList<String> ArrRel2 = new ArrayList<String>();
    static ArrayList<String> ArrDatPsJml = new ArrayList<String>();
    static ArrayList<String> arrsource = new ArrayList<String>();
    static ArrayList<String> arrtarget = new ArrayList<String>();
    static ArrayList<String> arrvalue = new ArrayList<String>();
    static ArrayList<Integer> arrradius = new ArrayList<Integer>();

//<----------------------------------Untuk Koneksi Database---------------------------------------------->//
    public Connection dbConnection() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public void collabGoF() {

        System.out.println("Start Execution Java !");

        Collab db = new Collab();
//----------------------------------------------------------------------------------------------------------//

//<---------------------------------Untuk menjalankan query database------------------------------------>//
        Connection connection;
        Statement statement;
        ResultSet resultSet;

        String query = "SELECT author, affiliation, string_agg(subject, ';')AS subject, dataset_id, datasetversion_ida  FROM(\n"
                + "\n"
                + "SELECT author, affiliation, subject, ttt1.dataset_id, ttt1.datasetversion_ida  FROM(\n"
                + "\n"
                + "--\n"
                + "SELECT * FROM(\n"
                + "\n"
                + "SELECT tath.string_agg AS author, \n"
                + "       taff.string_agg AS affiliation, \n"
                + "       tasub.strvalue AS subject,\n"
                + "       tath.displayorder, \n"
                + "       tath.parentdatasetfield_id, \n"
                + "       tath.datasetversion_id AS datasetversion_ida\n"
                + "FROM   (SELECT t1.string_agg, \n"
                + "               t1.parentdatasetfield_id, \n"
                + "               t1.displayorder, \n"
                + "               datasetfield.datasetversion_id \n"
                + "        FROM   (SELECT TRIM(datasetfieldvalue.value) AS string_agg, \n"
                + "                       datasetfieldcompoundvalue.parentdatasetfield_id AS parentdatasetfield_id, \n"
                + "                       datasetfield.datasetversion_id, \n"
                + "                       datasetfieldcompoundvalue.displayorder AS displayorder \n"
                + "                FROM   datasetfieldvalue, \n"
                + "                       datasetfield, \n"
                + "                       datasetfieldcompoundvalue \n"
                + "                GROUP  BY datasetfieldcompoundvalue.parentdatasetfield_id, \n"
                + "                          datasetfieldvalue.datasetfield_id = datasetfield.id, \n"
                + "                          datasetfield.parentdatasetfieldcompoundvalue_id = datasetfieldcompoundvalue.id, \n"
                + "                          datasetfield.datasetfieldtype_id = 9, \n"
                + "                          datasetfield.datasetversion_id, \n"
                + "                          datasetfieldvalue.value, \n"
                + "                          datasetfieldcompoundvalue.displayorder \n"
                + "                HAVING datasetfieldvalue.datasetfield_id = datasetfield.id \n"
                + "                       AND datasetfield.parentdatasetfieldcompoundvalue_id = datasetfieldcompoundvalue.id \n"
                + "                       AND datasetfieldcompoundvalue.parentdatasetfield_id IN ( \n"
                + "                           SELECT datasetfield.id \n"
                + "						   FROM datasetfield \n"
                + "                           WHERE datasetfield.datasetfieldtype_id = 8) \n"
                + "                       AND datasetfield.datasetfieldtype_id = 9 \n"
                + "                ORDER  BY datasetfieldcompoundvalue.parentdatasetfield_id) AS t1 \n"
                + "               LEFT OUTER JOIN datasetfield \n"
                + "                            ON ( t1.parentdatasetfield_id = datasetfield.id )\n"
                + "	) AS tath \n"
                + "\n"
                + "	-- LEFT JOIN dengan Subject\n"
                + "\n"
                + "LEFT JOIN (SELECT datasetfield.datasetversion_id, string_agg(strvalue,'==') AS strvalue\n"
                + "            FROM   datasetfield_controlledvocabularyvalue \n"
                + "            \n"
                + "            LEFT JOIN datasetfield ON ( datasetfield_controlledvocabularyvalue.datasetfield_id = datasetfield.id ) \n"
                + "			LEFT JOIN controlledvocabularyvalue ON ( datasetfield_controlledvocabularyvalue.controlledvocabularyvalues_id = controlledvocabularyvalue.id ) \n"
                + "			GROUP BY datasetfield.datasetversion_id, datasetfield.datasetfieldtype_id = 20 \n"
                + "			HAVING  datasetfield.datasetfieldtype_id = 20 \n"
                + "			ORDER  BY datasetversion_id\n"
                + "		) AS tasub\n"
                + "		ON ( tath.datasetversion_id = tasub.datasetversion_id )  \n"
                + "	\n"
                + "       --LEFTJOIN DENGAN AFFILIATION \n"
                + "\n"
                + "LEFT JOIN  (SELECT  	t1.string_agg, \n"
                + "			t1.parentdatasetfield_id, \n"
                + "			t1.displayorder, \n"
                + "			datasetfield.datasetversion_id \n"
                + "        FROM   (SELECT TRIM(datasetfieldvalue.value) AS string_agg, \n"
                + "                       datasetfieldcompoundvalue.parentdatasetfield_id AS parentdatasetfield_id, \n"
                + "                       datasetfield.datasetversion_id, \n"
                + "                       datasetfieldcompoundvalue.displayorder AS displayorder \n"
                + "                FROM   datasetfieldvalue, \n"
                + "                       datasetfield, \n"
                + "                       datasetfieldcompoundvalue \n"
                + "                GROUP  BY datasetfieldcompoundvalue.parentdatasetfield_id, \n"
                + "                          datasetfieldvalue.datasetfield_id = datasetfield.id, \n"
                + "                          datasetfield.parentdatasetfieldcompoundvalue_id = datasetfieldcompoundvalue.id, \n"
                + "                          datasetfield.datasetfieldtype_id = 10, \n"
                + "                          datasetfield.datasetversion_id, \n"
                + "                          datasetfieldvalue.value, \n"
                + "                          datasetfieldcompoundvalue.displayorder \n"
                + "                HAVING datasetfieldvalue.datasetfield_id = datasetfield.id \n"
                + "                       AND datasetfield.parentdatasetfieldcompoundvalue_id = datasetfieldcompoundvalue.id \n"
                + "                       AND datasetfieldcompoundvalue.parentdatasetfield_id IN ( \n"
                + "                           SELECT datasetfield.id \n"
                + "						   FROM datasetfield \n"
                + "                           WHERE datasetfield.datasetfieldtype_id = 8) \n"
                + "                       AND datasetfield.datasetfieldtype_id = 10 \n"
                + "                ORDER  BY datasetfieldcompoundvalue.parentdatasetfield_id) AS t1 \n"
                + "               LEFT OUTER JOIN datasetfield \n"
                + "                            ON ( t1.parentdatasetfield_id = datasetfield.id )\n"
                + "	)  AS taff \n"
                + "    \n"
                + "	ON ( tath.parentdatasetfield_id = taff.parentdatasetfield_id AND tath.displayorder = taff.displayorder ) \n"
                + "\n"
                + "--\n"
                + ") tab1\n"
                + "\n"
                + "LEFT JOIN (SELECT dataset_id, id AS datasetversion_id, lastupdatetime FROM datasetversion) tab2 \n"
                + "ON (tab1.datasetversion_ida = tab2.datasetversion_id) \n"
                + ")AS ttt1\n"
                + "--------AAA\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "\n"
                + "--INNER JOIN\n"
                + "RIGHT OUTER JOIN\n"
                + "--LEFT OUTER JOIN\n"
                + "(SELECT tauthor2.dataset_id, MAX(tauthor2.lastupdatetime) AS maxlastupdatetime FROM\n"
                + "	(\n"
                + "	--Utama2\n"
                + "	SELECT DISTINCT t2.string_agg AS string_agg, t2.datasetversion_id AS datasetversion_id, t2.parentdatasetfield_id AS parentdatasetfield_id, datasetversion.dataset_id AS dataset_id, datasetversion.lastupdatetime AS lastupdatetime\n"
                + "	FROM\n"
                + "		(\n"
                + "		SELECT t1.string_agg, t1.parentdatasetfield_id, datasetfield.datasetversion_id\n"
                + "		FROM \n"
                + "			(\n"
                + "			SELECT  string_agg(TRIM(datasetfieldvalue.value), ';') AS string_agg, datasetfieldcompoundvalue.parentdatasetfield_id AS parentdatasetfield_id, datasetfield.datasetversion_id\n"
                + "			FROM datasetfieldvalue, datasetfield, datasetfieldcompoundvalue\n"
                + "			GROUP BY datasetfieldcompoundvalue.parentdatasetfield_id  , datasetfieldvalue.datasetfield_id = datasetfield.id , datasetfield.parentdatasetfieldcompoundvalue_id = datasetfieldcompoundvalue.id , datasetfield.datasetfieldtype_id = 9, datasetfield.datasetversion_id\n"
                + "			HAVING datasetfieldvalue.datasetfield_id = datasetfield.id \n"
                + "				AND datasetfield.parentdatasetfieldcompoundvalue_id = datasetfieldcompoundvalue.id  \n"
                + "				AND datasetfieldcompoundvalue.parentdatasetfield_id IN (SELECT datasetfield.id FROM datasetfield WHERE datasetfield.datasetfieldtype_id = 8)\n"
                + "				AND datasetfield.datasetfieldtype_id = 9\n"
                + "			ORDER BY datasetfieldcompoundvalue.parentdatasetfield_id\n"
                + "			)	\n"
                + "			AS t1\n"
                + "			LEFT OUTER JOIN datasetfield ON (t1.parentdatasetfield_id = datasetfield.id) \n"
                + "		)\n"
                + "		AS t2\n"
                + "		LEFT OUTER JOIN datasetversion ON (t2.datasetversion_id = datasetversion.id)\n"
                + "	--/Utama2\n"
                + "	) \n"
                + "	AS tauthor2 GROUP BY tauthor2.dataset_id) \n"
                + "	AS ttt2\n"
                + "	ON ttt1.dataset_id = ttt2.dataset_id AND ttt1.lastupdatetime = ttt2.maxlastupdatetime ORDER BY ttt1.dataset_id \n"
                + "\n"
                + ")A GROUP BY author, affiliation, subject, dataset_id, datasetversion_ida ORDER BY dataset_id;";

        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String url = "jdbc:postgresql://localhost:5432/dblipi";
            String user = "postgres";
            String password = "mtaufiqakbar";
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()) {
                String Au = rs.getString("author");
                String Af = rs.getString("affiliation");
                String Id = rs.getString("dataset_id");
                String Sb = rs.getString("subject");

//<-----------Pendeklarasian method yang akan digunakan------------------------->//
                db.Proses(Id, Au, Af, Sb);
                db.ProsesAuAf(Id, Au, Af);

            }

//<----------------------------- Untuk mengecek daftar nama author----------------------------------->//
            /////////////////////////////////////////////////////////////////////////////////////////////////////
            for (int p = 0; p <= (ArrRel.size() - 1); p++) {
                String[] GabAuth = ArrRel.get(p).split("\\s*==\\s*");
                ArrRel1.add(GabAuth[0]);
                ArrRel2.add(GabAuth[1]);
            }

            for (int i = 0; i <= (ArrDat.size() - 1); i++) {
                int val = 0, kerjasama = 0;
                keywords.add(ArrDat.get(i));
//                System.out.println("Dat : " + i + " || Isi keywords :|" + keywords.get(0) + "|");//#
                String kalPsJml = "";
                for (int j = 0; j <= (ArrRel1.size() - 1); j++) {
//                    System.out.println("|"+ArrRel1.get(j) +"|__|"+ keywords.get(0)+"| " + (ArrRel1.get(j)==keywords.get(0)) +" | "+ArrRel1.get(j).equals(keywords.get(0)) );
                    if (ArrRel1.get(j).equals(keywords.get(0))) {
//                        System.out.println(" Kiri || Ada di Idx- : " + j + "|| Pasangan Collab : " + ArrRel2.get(j) + "|| Jumlah Kolaborasi : " + ArrRelVal.get(j));//#
                        val = val + ArrRelVal.get(j);
                        kerjasama++;
                        kalPsJml = kalPsJml + ";" + ArrRel2.get(j) + "=" + ArrRelVal.get(j);
                    }
                    if (ArrRel2.get(j).equals(keywords.get(0))) {
//                        System.out.println(" Kanan || Ada di Idx- : " + j + "|| Pasangan Collab : " + ArrRel1.get(j) + "|| Jumlah Kolaborasi : " + ArrRelVal.get(j));//#
                        val = val + ArrRelVal.get(j);
                        kerjasama++;
                        kalPsJml = kalPsJml + ";" + ArrRel1.get(j) + "=" + ArrRelVal.get(j);
                    }
                }

                keywords.remove(0);
                ArrDatVal.add(val);
//                System.out.println("kalPsJml : "+kalPsJml);//#
                ArrDatPsJml.add(kalPsJml);
//                System.out.println("Value Jml Jurnal : " + val + " || Kerjasama : " + kerjasama); //#
//                System.out.println("=====================BATAS====================");//#
            }

            List<Sorting> sort = new ArrayList<Sorting>();
            for (int c = 0; c < ArrDatVal.size(); c++) {
                sort.add(new Sorting(c, ArrDatVal.get(c)));
            }

            Collections.sort(sort);
            Collections.reverse(sort);
            int idx = sort.get(0).idx;
//            System.out.println("Index : " + idx + " || ArrDat : "+ ArrDat.get(idx)+ " || ArrDatVal : "+ ArrDatVal.get(idx) +" || ArrDatPsJml : "+ ArrDatPsJml.get(idx));//#

            //split
            String[] arrPsJml = ArrDatPsJml.get(idx).split("\\s*;\\s*");
//            System.out.println(arrPsJml.length);//#
//            System.out.println("|" + arrPsJml[0] + "|" + arrPsJml[1] + "|" + arrPsJml[66] + "|");//#

///////////////////////////////////untuk membuat link////////////////////////////////
            for (int i = 1; i <= (arrPsJml.length - 1); i++) {
//                System.out.println(arrPsJml[i]);//#
                String[] divide = arrPsJml[i].split("\\s*=\\s*");
                target = divide[0];
                value = divide[1];
                source = ArrDat.get(idx);
                arrtarget.add(target);
                arrsource.add(source);
                arrvalue.add(value);
                System.out.println("source : " + source + ", target : " + target + ", value : " + value);
            }

///////////////////////////////////untuk membuat node////////////////////////////////
            JSONObject baseJson1new = new JSONObject();
            JSONObject baseJson2new = new JSONObject();
            JSONObject baseJson3new = new JSONObject();

            JSONArray nodesnew = new JSONArray();
            JSONArray linksnew = new JSONArray();

            ArrayList<String> jdaftarauthnew = new ArrayList<String>();
            ArrayList<String> jdaftarsubjnew = new ArrayList<String>();
            ArrayList<String> jdaftaraffnew = new ArrayList<String>();

            jdaftarauthnew.addAll(arrsource);
            jdaftarauthnew.addAll(arrtarget);
            List<Object> jdaftarauthnew1 = jdaftarauthnew.stream().distinct().collect(Collectors.toList());
            for (Object i : jdaftarauthnew1) {
                System.out.println("jdaftarauth : " + i);
                int IdxDatVal = ArrDat.indexOf(i);
                arrradius.add(ArrDatVal.get(IdxDatVal));

            }

            for (int i = 0; i <= (jdaftarauthnew1.size() - 1); i++) {

                JSONObject childrenNodes = new JSONObject();

                childrenNodes.put("radius", arrradius.get(i) / 10);
                childrenNodes.put("id", jdaftarauthnew1.get(i));
                childrenNodes.put("name", jdaftarauthnew1.get(i));
//                childrenNodes.put("subject", ArrRelSub.get(i));
//                childrenNodes.put("affiliation", ArrRelAff.get(i));
//                childrenNodes.put("group", ArrRelAff.get(i));
                nodesnew.add(childrenNodes);
            }

            for (int i = 0; i <= (arrPsJml.length - 2); i++) {

                JSONObject childrenLinks = new JSONObject();
                childrenLinks.put("source", arrsource.get(i));
                childrenLinks.put("target", arrtarget.get(i));
                childrenLinks.put("value", arrvalue.get(i));
                linksnew.add(childrenLinks);
            }

//            baseJson1.put("nodes", nodes);
//            System.out.println(baseJson1.toString());
//            baseJson2.put("links", links);
//            System.out.println(baseJson2.toString());
            baseJson3new.put("nodes", nodesnew);
            baseJson3new.put("links", linksnew);
            System.out.println(baseJson3new.toString());

            /////////////////////////Cetak Json////////////////////////////////////////////
            FileWriter writer = null;
            try {
                writer = new FileWriter("D:/SKRIPSI/GraphofRelation_REVISI/GraphofRelation/src/main/webapp/graph.json");
                writer.write(baseJson3new.toString());

            } catch (IOException e) {
                e.printStackTrace();

            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

        } catch (SQLException es) {
            es.printStackTrace();
        }

        return;
    }

//<---------------------------------Sorting Data---------------------------------->//           
//            System.out.println("Size ArrRel :" + ArrRel.size() + " , Size ArrRelVal :" + ArrRelVal.size());
//
//            for (int i = 0; i < ArrRel.size(); i++) {
//                System.out.println("Idx : " + i + " ,REL : " + ArrRel.get(i) + " ,VAL : " + ArrRelVal.get(i) + " , AFF : " + ArrRelAff.get(i));
//            }
//
//            List<Sort> sorts = new ArrayList<Sort>();
//            for (int s = 0; s < ArrRelVal.size(); s++) {
//                sorts.add(new Sort(s, ArrRelVal.get(s)));
//            }
//
//            //Proses menentukan ArrRelVal setelah dilakukan sorting ascending(kecil-besar)        
//            Collections.sort(sorts);
//
//            //Proses mendapatkan nilai ArrRelVal yang sudah dilakukan sorting descending(besar-kecil)
//            Collections.reverse(sorts);
//
////<-------------------Hasil Sorting Data 10 Kolaborasi Terbanyak------------------------------>//                
//            List<Sort> limit = new ArrayList<Sort>(sorts.subList(0, 10));
//
//            for (Sort s : limit) {
//                System.out.println("Hasil Sort, Val : " + s.val + " || Collab: " + ArrRel.get(s.idx) + " || Affiliation: " + ArrRelAff.get(s.idx) + " || Subject: " + ArrRelSub.get(s.idx));
//
////<-----------------------------------------Menentukan Kode Affilation-------------------------------------------------->//                
//                JName.add(ArrRel.get(s.idx));
//                JValue.add(s.val);
//                JSubject.add(ArrRelSub.get(s.idx));
//                JAff.add(ArrRelAff.get(s.idx));
//                if (!JKamusAff.contains(ArrRelAff.get(s.idx))) {
//                    JKamusAff.add(ArrRelAff.get(s.idx));
//                }
    private void Proses(String Id, String Au, String Af, String Sb) {
// Jika tidak mengandung Array Id
        if (ArrId.indexOf(Id) == -1) { //Jika False (tidak ada)
            ArrId.add(Id);
            ArrAu.add(Au);
            ArrAf.add(Af);
            ArrSb.add(Sb);

        } else { //Jika True (mengandung Array Id)

            this.Idx = ArrId.indexOf(Id);
            this.SourceIdx = this.Idx;
            ArrId.add(Id);
            ArrAu.add(Au);
            ArrAf.add(Af);
            ArrSb.add(Sb);
            this.TempLastIdx = (ArrId.size() - 1);

            // Perulangan untuk menentukan kolaborasi antar author
            for (int i = SourceIdx; i < TempLastIdx; i++) {
                for (int j = i + 1; j <= TempLastIdx; j++) {
                    if (i == j) {
                        continue;
                    }

                    ProsesLink(i, j);
                }
            }
        }

    }

    private void ProsesLink(int i, int j) {
        if (ArrRel.contains(ArrAu.get(i) + "==" + ArrAu.get(j)) == false) {
            ArrRel.add(ArrAu.get(i) + "==" + ArrAu.get(j)); //Tambahkan ArrRel
            IdxArrRel = (ArrRel.size() - 1);
            ProsesArrRel(1, IdxArrRel, i); //mengirim proses arrrel baik ada ataupun tidak ada

            //Jika True (mengandung nilai array yang sama (mengandung nilai i==j)  
        } else if (ArrRel.contains(ArrAu.get(i) + "==" + ArrAu.get(j)) == true) {
            IdxArrRel = ArrRel.indexOf(ArrAu.get(i) + "==" + ArrAu.get(j));//Cari Ada di index ke berapa
            ProsesArrRel(2, IdxArrRel, i);

        } else if (ArrRel.contains(ArrAu.get(j) + "==" + ArrAu.get(i)) == true) {
            IdxArrRel = ArrRel.indexOf(ArrAu.get(j) + "==" + ArrAu.get(i));//Cari Ada di index ke berapa
            ProsesArrRel(2, IdxArrRel, i);

        }
    }

//            for (int x = 0; x <= JKamusAff.size() - 1; x++) {
//                JCodeKamusAff.add(x);
//            }
//            for (String a : JKamusAff) {
//                System.out.println("JKamusAff :" + a);
//            }
//            for (int b : JCodeKamusAff) {
//                System.out.println("JCodeKamusAff :" + b);
//            }
//            for (String c : JName) {
//                System.out.println("JName :" + c);
//            }
//            for (String c : JAff){
//                System.out.println("JAff :" + c);
//            }
//            for (int c : JValue) {
//                System.out.println("JValue :" + c);
//            }
//            for (String c : JSubject) {
//                System.out.println("JSubject :" + c);
//            }
//            SortingData s = new SortingData();
//            s.Sorting(JKamusAff, JCodeKamusAff);
//<-----------------------------------------Convert to JSON-------------------------------------------------->//                
//            JSONObject baseJson1 = new JSONObject();
//            JSONObject baseJson2 = new JSONObject();
//            JSONObject baseJson3 = new JSONObject();
//
//            JSONArray nodes = new JSONArray();
//            JSONArray links = new JSONArray();
//
//            ArrayList<String> jdaftarauth = new ArrayList<String>();
//            ArrayList<String> jdaftarsubj = new ArrayList<String>();
//            ArrayList<String> jdaftaraff = new ArrayList<String>();
//
//            for (int i = 0; i <= (JName.size() - 1); i++) {
//                String[] GabAuth = JName.get(i).split("\\s*==\\s*");
//////                System.out.println("Kiri : " + GabAuth[0] + " || Kanan : " + GabAuth[1]);
//                if (!jdaftarauth.contains(GabAuth[0])) {
//                    jdaftarauth.add(GabAuth[0]);
//                    jdaftarsubj.add(JSubject.get(i));
//                    jdaftaraff.add(JAff.get(i));
//                }
//                if (!jdaftarauth.contains(GabAuth[1])) {
//                    jdaftarauth.add(GabAuth[1]);
//                    jdaftarsubj.add(JSubject.get(i));
//                    jdaftaraff.add(JAff.get(i));
//                }
//            }
    //            for (String jdaut : jdaftarauth) { //Daftar author node
    //                System.out.println("DaftarAuthNode : " + jdaut);
    //            }
    //            for (String jsubj : jdaftarsubj) { //Daftar subject node
    //                System.out.println("DaftarSubjNode : " + jsubj);
    //            }
    //            for (String jaff : jdaftaraff) { //Daftar subject node
    //                System.out.println("DaftarAffNode : " + jaff);
    //            }
//            for (int i = 0; i <= (jdaftarauth.size() - 1); i++) {
//
//                JSONObject childrenNodes = new JSONObject();
//
//                childrenNodes.put("radius", 20);
//                childrenNodes.put("id", jdaftarauth.get(i));
//                childrenNodes.put("name", jdaftarauth.get(i));
//                childrenNodes.put("subject", jdaftarsubj.get(i));
//                int iidx = ArrDat.indexOf(jdaftarauth.get(i)); //Mendapatkan index asal afiliasi dari nama, ArrDat - ArrDaf
//                String iidxNameAff = ArrDaf.get(iidx);   //iidxNameAff adalah nama afiliasi yang didapatkan
//                childrenNodes.put("affiliation", iidxNameAff);
//                childrenNodes.put("affiliation", jdaftaraff.get(i));
//                childrenNodes.put("group", jdaftaraff.get(i));
//                nodes.add(childrenNodes);
//            }
//
//            for (int i = 0; i <= (JName.size() - 1); i++) {
//                String[] GabAuth = JName.get(i).split("\\s*==\\s*");
////                System.out.println("Kiri : " + GabAuth[0] + " || Kanan : " + GabAuth[1]);
//                JSONObject childrenLinks = new JSONObject();
//                childrenLinks.put("source", GabAuth[0]);
//                childrenLinks.put("target", GabAuth[1]);
//                childrenLinks.put("value", JValue.get(i));
//                links.add(childrenLinks);
//            }
//            baseJson1.put("nodes", nodes);
//            System.out.println(baseJson1.toString());
//            baseJson2.put("links", links);
////            System.out.println(baseJson2.toString());
//            baseJson3.put("nodes", nodes);
//            baseJson3.put("links", links);
//            System.out.println(baseJson3.toString());
//<---------------------------------------------Create File JSON----------------------------------------------------->//                
//            FileWriter writer = null;
//            try {
//                writer = new FileWriter("D:/Taufiq Data/SKRIPSI/GOF/src/main/webapp/graph.json");
//                writer.write(baseJson3.toString());
//
//            } catch (IOException e) {
//                e.printStackTrace();
//
//            } finally {
//                if (writer != null) {
//                    try {
//                        writer.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//        } catch (SQLException es) {
//            es.printStackTrace();
//        }
//
//        return;
//    }
//<-------------------------Method Proses untuk menentukan perulangan kolaborasi antara author------------------------------------->//
//    void Proses(String Id, String Au, String Af, String Sb) {
////              System.out.println(Id +"  ||  "+ Au+"  ||  "+Af+"  ||  "+Sb);
//
////        this.Id = a;
////        this.Au = b;
////        this.Af = c;
////        this.Sb = d;
//        // Jika tidak mengandung Array Id
//        if (ArrId.indexOf(Id) == -1) { //Jika False (tidak ada)
//            ArrId.add(Id);
//            ArrAu.add(Au);
//            ArrAf.add(Af);
//            ArrSb.add(Sb);
//
//        } else { //Jika True (mengandung Array Id)
//
//            this.Idx = ArrId.indexOf(Id);
//            this.SourceIdx = this.Idx;
//            ArrId.add(Id);
//            ArrAu.add(Au);
//            ArrAf.add(Af);
//            ArrSb.add(Sb);
//            this.TempLastIdx = (ArrId.size() - 1);
//
//            // Perulangan untuk menentukan kolaborasi antar author
//            for (int i = SourceIdx; i < TempLastIdx; i++) {
//                for (int j = i + 1; j <= TempLastIdx; j++) {
//                    if (i == j) {
//                        continue;
//                    }
//
//                    ProsesLink(i, j);
//                }
//            }
//        }
//        System.out.println("ArrId :" + ArrId);
//        System.out.println("ArrRel : " + ArrRel);
//        System.out.println("ArrRelVal : " + ArrRelVal);
//        System.out.println("ArrRelId : " + ArrRelId);
//        System.out.println("ArrSub :" + ArrRelSub);
//        System.out.println("ArrAff :" + ArrRelAff);
//        System.out.println("======================================**************======================================");
    private void ProsesArrRel(int tipe, int IdxArrRel, int i) {
        if (tipe == 1) {
            ArrRelId.add(ArrId.get(i));
            ArrRelVal.add(1);
            ArrVal.add(1);
            ArrRelSub.add(ArrSb.get(i));
            ArrRelAff.add(ArrAf.get(i));
        } else {
            //Jika sudah menemukan nilai yg sudah ada maka
            if (ArrRelId.get(IdxArrRel) != ArrId.get(i)) {
                //Memperbaharui index dengan nilai yang sama
                ArrRelId.set(IdxArrRel, ArrId.get(i));

                int Val = (Integer.valueOf(ArrRelVal.get(IdxArrRel) + 1));
                ArrRelVal.set(IdxArrRel, Val);

                //Menentukan subject apabila bernilai berbeda maka akan ditambahkan dengan "=="
                if (ArrRelSub.get(IdxArrRel) != ArrSb.get(i)) {
                    String Sub = (String.valueOf(ArrRelSub.get(IdxArrRel) + "==" + ArrSb.get(i)));
                    Sub = Arrays.stream(Sub.split("==")).distinct().collect(Collectors.joining("=="));

                    ArrRelSub.set(IdxArrRel, Sub);

                }

            }
        }
    }

    private void ProsesAuAf(String Id, String Au, String Af) {
        if (!ArrDat.contains(Au)) {
            ArrDat.add(Au);
            ArrDaf.add(Af);
        }

    }

    public static boolean hasKey(String key) {
        return keywords.stream().filter(k -> key.contains(k)).collect(Collectors.toList()).size() > 0;
    }

}
//<------------------------------------Method Proses Link untuk menentukan kolaborasi author (i) dan (j)-------------------------------------------------->//
// Untuk menentukan apakah pada ArrRel terdiri dari Author i == Author j atau tidak     
//    private void ProsesLink(int i, int j) {
////        this.TempArrIdSaatIni = ArrId.get(ArrId.size() - 1);
//
//        //Jika False (tidak mengandung isi tsb) && masih dijurnal yg sama
//        if (ArrRel.contains(ArrAu.get(i) + "==" + ArrAu.get(j)) == false) {
//            ArrRel.add(ArrAu.get(i) + "==" + ArrAu.get(j)); //Tambahkan ArrRel
//            IdxArrRel = (ArrRel.size() - 1);
//            ProsesArrRel(1, IdxArrRel, i); //mengirim proses arrrel baik ada ataupun tidak ada
//
//            //Jika True (mengandung nilai array yang sama (mengandung nilai i==j)  
//        } else if (ArrRel.contains(ArrAu.get(i) + "==" + ArrAu.get(j)) == true) {
//            IdxArrRel = ArrRel.indexOf(ArrAu.get(i) + "==" + ArrAu.get(j));//Cari Ada di index ke berapa
//            ProsesArrRel(2, IdxArrRel, i);
//
//        } else if (ArrRel.contains(ArrAu.get(j) + "==" + ArrAu.get(i)) == true) {
//            IdxArrRel = ArrRel.indexOf(ArrAu.get(j) + "==" + ArrAu.get(i));//Cari Ada di index ke berapa
//            ProsesArrRel(2, IdxArrRel, i);
//
//        }
//    }
//
////<---------------------------Method ProsesArrRel untuk memproses relasi array dari method Proses--------------------------------------->//
//// Untuk memproses Array Relasi apabila ditemukan nilai yang sama pada id yang berbeda
//    private void ProsesArrRel(int tipe, int IdxArrRel, int i) {
//
//        //jika tidak ada maka val ditambahkan 1
//        if (tipe == 1) {
//            ArrRelId.add(ArrId.get(i));
//            ArrRelVal.add(1);
//            ArrVal.add(1);
//            ArrRelSub.add(ArrSb.get(i));
//            ArrRelAff.add(ArrAf.get(i));
//        } else {
//            //Jika sudah menemukan nilai yg sudah ada maka
//            if (ArrRelId.get(IdxArrRel) != ArrId.get(i)) {
//                //Memperbaharui index dengan nilai yang sama
//                ArrRelId.set(IdxArrRel, ArrId.get(i));
//
//                int Val = (Integer.valueOf(ArrRelVal.get(IdxArrRel) + 1));
//                ArrRelVal.set(IdxArrRel, Val);
//
//                //Menentukan subject apabila bernilai berbeda maka akan ditambahkan dengan "=="
//                if (ArrRelSub.get(IdxArrRel) != ArrSb.get(i)) {
//                    String Sub = (String.valueOf(ArrRelSub.get(IdxArrRel) + "==" + ArrSb.get(i)));
//                    Sub = Arrays.stream(Sub.split("==")).distinct().collect(Collectors.joining("=="));
//
//                    ArrRelSub.set(IdxArrRel, Sub);
//
//                }
//
//            }
//        }
//    }
//
//    private void ProsesAuAf(String Id, String Au, String Af) {
//        if (!ArrDat.contains(Au)) {
//            ArrDat.add(Au);
//            ArrDaf.add(Af);
//        }
//
//    }

