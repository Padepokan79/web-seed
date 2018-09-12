/**
 * 
 */
package app.controllers.sdm;

//import java.sql.Date;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.javalite.activejdbc.LazyList;
import org.javalite.activejdbc.Model;
import org.javalite.common.Convert;

import com.ibm.icu.util.Calendar;

import app.controllers.sdm.CourseController.CourseDTO;
import app.models.ContractType;
import app.models.Course;
import app.models.Education;
import app.models.Employment;
import app.models.Gender;
import app.models.Health;
import app.models.Profiling;
import app.models.Religion;
import app.models.Sdm;
import app.models.SdmLanguage;
import app.models.SdmLvl;
import core.io.model.CorePage;
import core.io.model.DTOModel;
import core.io.model.PagingParams;
import core.javalite.controllers.CRUDController;
import java.text.SimpleDateFormat;

/**
 *web-seed
 * MengelolaSdmController.java
 ----------------------------
 * @author Ryan Ahmad Nuriana
 * 13.51.48 24 Jul 2018
 */
public class MengelolaSdmController extends CRUDController<Sdm> {

	public class SdmDTO extends DTOModel {
		public int sdm_id;
		public int sdmlvl_id;
		public int religion_id;
		public int health_id;
		public int contracttype_id;
		public String sdm_level;
		public String contracttype;
		public int gender_id;
		public String religion_name;
		public String sdm_name;
		public String sdm_nik;
		public String sdm_ktp;
		public String sdm_contractloc;
		public String sdm_objective;
		public String sdm_address;
		public String sdm_email;
		public String sdm_placebirth;
		public String sdm_postcode;
		public String sdm_phone;
		public String sdm_image;
		public String sdm_linkedin;
		public String sdm_startcontract;
		public String sdm_endcontract;
		public String sdm_status;
		public int norut;
		public String sdm_datebirth;
		public String sdm_notification;
		
	}

	
	@Override
	public CorePage customOnReadAll(PagingParams params) throws Exception {
	
	/*
	 * Updated by Nurdhiat Chaudhary Malik
	 * 07 Agustus 2018
	 */
	DateFormat dateAwal = new SimpleDateFormat("dd/MM/yyyy");
	DateFormat dateAkhir = new SimpleDateFormat("dd/MM/yyyy");
		
	List<Map<String, Object>> listMapSdm = new ArrayList<Map<String, Object>>();
	LazyList<Sdm> listSdm = (LazyList<Sdm>)this.getItems(params);	
	params.setOrderBy("sdm_id");

	Long totalItems = this.getTotalItems(params);

	/*
	 * Updated by Alifhar Juliansyah
	 * 29/08/2018
	 */
	int noruts = 1;
	if(params.limit() != null)
		noruts = params.limit().intValue() * params.offset().intValue() + 1;
	
		for (Sdm sdm : listSdm) {
			SdmLvl sdmlvl = sdm.parent(SdmLvl.class);
			ContractType ct = sdm.parent(ContractType.class);
			Gender gender = sdm.parent(Gender.class);
			Religion religion = sdm.parent(Religion.class);
			Health health = sdm.parent(Health.class);
			
			/*
			 * Updated by Alifhar Juliansyah
			 * 29/08/2018
			 */
			Map<String, Object> temp = sdm.toMap();
			temp.remove("sdm_status");
			temp.remove("sdm_startcontract");
			temp.remove("sdm_endcontract");
			temp.remove("sdm_datebirth");
			
			SdmDTO dto = new SdmDTO();
			dto.fromModelMap(temp);
			dto.norut = noruts;
			noruts++;
						
			/*
			 * Updated by Nurdhiat Chaudhary Malik
			 * 08 Agustus 2018
			 */
			java.util.Date judAwl = dateAwal.parse(getCurrentDate());
			java.util.Date judAkhir = dateAkhir.parse(getConvertBulan(sdm.get("sdm_endcontract").toString()));
			java.sql.Date tglAwal = new java.sql.Date(judAwl.getTime());
			java.sql.Date tglAkhir = new java.sql.Date(judAkhir.getTime());
			Date TGLAwal = tglAwal;
            Date TGLAkhir = tglAkhir;
            Calendar cal1 = Calendar.getInstance();
            Calendar cal2 = Calendar.getInstance();
            cal1.setTime(TGLAwal);
            cal2.setTime(TGLAkhir);
            String diff = Convert.toString(mothsBetween(cal1, cal2));
            
            if (Integer.parseInt(diff) == 0) {
            	dto.sdm_notification = "black"; // notif warna hitam
            	
			}else if(Integer.parseInt(diff) <= 1) {
				dto.sdm_notification = "red"; // notif warna merah
				
			}else if(Integer.parseInt(diff) <= 2) {
				dto.sdm_notification = "yellow"; // notif warna kuning
				
			}else if(Integer.parseInt(diff) <= 4) {
				dto.sdm_notification = "green"; // notif warna hijau
				
			}else if(Integer.parseInt(diff) > 4) {
				dto.sdm_notification = "grey"; // notif warna grey
			}
            

//			dto.sdm_startcontract =getConvertBulan(sdm.get("sdm_startcontract").toString());
//			dto.sdm_endcontract = getConvertBulan(sdm.get("sdm_endcontract").toString());
//			dto.sdm_datebirth = getConvertBulan(sdm.get("sdm_datebirth").toString());
			
            dto.sdm_datebirth = Convert.toString(sdm.get("sdm_datebirth"));
			dto.sdm_startcontract = Convert.toString(sdm.get("sdm_startcontract"));
			dto.sdm_endcontract = Convert.toString(sdm.get("sdm_endcontract"));
			
			
            String status = Convert.toString(sdm.get("sdm_status"));
			if(status.equals("1")) {
				dto.sdm_status = "Active";
			}
			else {
				dto.sdm_status = "Non-Active";
			}
			dto.sdm_level = Convert.toString(sdmlvl.get("sdmlvl_name"));
			dto.contracttype = Convert.toString(ct.get("contracttype_name"));
			dto.sdm_contractloc = Convert.toString(sdm.get("sdm_contractloc"));
			
//			if(dto.sdm_contractloc == "1" ) {
//				dto.sdm_contractloc = "Bandung";
//			}
//			else
//			{
//				dto.sdm_contractloc = "Luar Bandung";
//			}
//			String date = Convert.toString("2018/09/08");
//			Date d1 = new SimpleDateFormat("yyyy-MM-dd").parse(date);
//			dto.sdm_datebirth =Convert.toString(d1);
			listMapSdm.add(dto.toModelMap());
		}
		
//		System.out.println((LazyList<Sdm>)this.getItems(params));
	
	return new CorePage(listMapSdm, totalItems);		
	}
	
	
	/*
	 * Updated by Nurdhiat Chaudhary Malik
	 * 09 Agustus 2018
	 */
	private static long mothsBetween(Calendar tanggalAwal, Calendar tanggalAkhir) {
        long lama = 0;
        Calendar tanggal = (Calendar) tanggalAwal.clone();
        while (tanggal.before(tanggalAkhir)) {
            tanggal.add(Calendar.DAY_OF_MONTH, 1);
            lama++;
        }
        
        if (lama > 30) {
        	lama = (lama)/30;
        	
		}else if(lama < 30 && lama > 0) {
			lama = 1;
		
		}else {
			lama = 0;
		}
        return lama;
        
    }
	
	/*
	 * Updated by Nurdhiat Chaudhary Malik
	 * 07 Agustus 2018
	 */
	public String getCurrentDate(){
	    final Calendar c = Calendar.getInstance();
	    int year, month, day;
	    year = c.get(Calendar.YEAR);
	    month = c.get(Calendar.MONTH);
	    day = c.get(Calendar.DATE);
	    return day + "/" + (month+1) + "/" + year;
	}
	
	/*
	 * Updated by Nurdhiat Chaudhary Malik
	 * 07 Agustus 2018
	 */
	public String getConvertBulan(String now) {
		String bulan[] = {"01","02","03","04","05","06","07","08","09","10","11","12"};
		String tanggal = now.substring(8,10);
		String bln = now.substring(5,7);
		String tahun = now.substring(0,4);
		String hasil =tanggal+"/"+bulan[Integer.parseInt(bln)-1]+"/"+tahun;
		System.out.println("cek : " + hasil);
		return hasil;
	}
		
		/*
		 * Updated (Commented) by Alifhar Juliansyah
		 * 15 August 2018, 11:02
		 */
//		@Override
//		public Map<String, Object> customOnInsert(Sdm item, Map<String, Object> mapRequest) throws Exception{		
//			Map<String, Object> result = super.customOnInsert(item, mapRequest);
//			Sdm sdm = Sdm.findFirst("Order by sdm_id desc");
//			
//			//input education
//			Education edu = new Education();
//			String nama = Convert.toString(mapRequest.get("edu_name"));
//			Integer degree = Convert.toInteger(mapRequest.get("degree_id"));
//			String jurusan = Convert.toString(mapRequest.get("edu_subject"));
//			Date masuk = Convert.toSqlDate(mapRequest.get("edu_startdate"));
//			Date keluar = Convert.toSqlDate(mapRequest.get("edu_startdate"));
//			edu.set("EDU_NAME", nama);
//			edu.set("DEGREE_ID", degree);
//			edu.set("EDU_SUBJECT", jurusan);
//			edu.set("EDU_STARTDATE", masuk);
//			edu.set("EDU_ENDDATE", keluar);
//			edu.set("sdm_id", sdm.getId());
//			edu.save();
//			
//			//input COURSE
//			Course course = new Course();
//			String kursus = Convert.toString(mapRequest.get("course_title"));
//			String penyelenggara = Convert.toString(mapRequest.get("course_provider"));
//			String tempat = Convert.toString(mapRequest.get("course_place"));
//			Date waktu = Convert.toSqlDate(mapRequest.get("course_date")); 
//			String durasi = Convert.toString(mapRequest.get("course_duration"));
//			int sertifikat = Convert.toInteger(mapRequest.get("course_cerificate"));
//			course.set("COURSE_TITLE", kursus);
//			course.set("COURSE_PROVIDER", penyelenggara);
//			course.set("COURSE_PLACE", tempat);
//			course.set("COURSE_DATE", waktu);
//			course.set("COURSE_DURATION", durasi);
//			course.set("COURSE_CERIFICATE", sertifikat);
//			course.set("sdm_id", sdm.getId());
//			course.save();
//			
//			//input employment
//			Employment employment = new Employment();
//			String perusahaan = Convert.toString(mapRequest.get("employment_corpname"));
//			Date start = Convert.toSqlDate(mapRequest.get("employment_startdate"));
//			Date end = Convert.toSqlDate(mapRequest.get("employment_enddate"));
//			String job = Convert.toString(mapRequest.get("employment_rolejob"));
//			employment.set("EMPLOYMENT_CORPNAME", perusahaan);
//			employment.set("EMPLOYMENT_STARTDATE", start);
//			employment.set("EMPLOYMENT_ENDDATE", end);
//			employment.set("EMPLOYMENT_ROLEJOB", job);
//			employment.set("sdm_id", sdm.getId());
//			employment.save();
//			
//			//input profiling
//			Profiling profiling = new Profiling();
//			String profile = Convert.toString(mapRequest.get("profiling_name"));
//			profiling.set("PROFILING_NAME", profile);
//			profiling.set("sdm_id", sdm.getId());
//			
//			//input SDMLANGUAGE
//			SdmLanguage sdml = new SdmLanguage();
//			int languageId = Convert.toInteger(mapRequest.get("language_id"));
//			sdml.set("language_id", languageId);
//			sdml.set("sdm_id", sdm.getId());
//			sdml.save();
//			
//			return result;
//		}
	
	 // Updated by Hendra Kuniawan
	 // 11/9/2018
	//update status sdm otomatis berdsarkan tanggal kontrak
	@Override
	public Map<String, Object> customOnUpdate(Sdm item, Map<String, Object> mapRequest) throws Exception {
		Map<String, Object> result = super.customOnUpdate(item, mapRequest);
		
		String startContract = Convert.toString(mapRequest.get("sdm_startcontract"));
		String endContract = Convert.toString(mapRequest.get("sdm_endcontract"));
		int sdmStatus;
		 
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	       
			Date currentDate = new Date();
		 
			System.out.println(sdf.format(currentDate));
		 
		    Date startContractDate = sdf.parse(startContract);
	        Date endContractDate = sdf.parse(endContract);

	        if (currentDate.compareTo(startContractDate) >= 0 && currentDate.compareTo(endContractDate) <= 0) {
	           sdmStatus = 1;
	        } else {
	        	sdmStatus = 0;
	        }
	        item.set("sdm_status", sdmStatus);
	        item.save();
	        
		return result;
	}
	

}
