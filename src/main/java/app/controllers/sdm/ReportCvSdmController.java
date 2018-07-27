package app.controllers.sdm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.javalite.common.Convert;
import org.javalite.common.JsonHelper;

import app.models.Sdm;
import app.util.ConvertToLowerCase;
import core.javalite.controllers.ReportController;

public class ReportCvSdmController extends ReportController{

	public ReportCvSdmController() {
		super("report-design/ReportCvSdm.jrxml");
	}

	@Override
	public Map<String, Object> getData() throws Exception {

		int sdmId = Convert.toInteger(param("$sdmId"));
		int nourut;
		
		List<Map> listData = new ArrayList<>();
		List<Map> listDataLanguage = new ArrayList<>();
		List<Map> listDataProfiling = new ArrayList<>();
		List<Map> listDataSkillSdm = new ArrayList<>();
		List<Map> listDataEduc = new ArrayList<>();
		List<Map> listDataCourse = new ArrayList<>();
		List<Map> listDataEmployment = new ArrayList<>();
		List<Map> listDataProject = new ArrayList<>();
		
		Map<String, Object> tampil = new HashMap<>();
		
		List<Map> dataFromQuery = Sdm.getDataCV(sdmId);
		for (Map map : dataFromQuery) {
//			tampil.put("HEALTH_STATUS", map.get("HEALTH_STATUS"));
			map.put("SDM_DATEBIRTH", getConvertBulan(map.get("SDM_DATEBIRTH").toString()));
			listData.add(map);
		}
		
		List<Map> dataFromQueryLanguage = Sdm.getDataLanguage(sdmId);
		for (Map map : dataFromQueryLanguage) { 
			listDataLanguage.add(map);
		}
		
		List<Map> dataFromQueryProfiling = Sdm.getDataProfiling(sdmId);
		for (Map map : dataFromQueryProfiling) { 
			listDataProfiling.add(map);
		}
		
		List<Map> dataFromQuerySkillSdm = Sdm.getDataSkillSdm(sdmId);
		nourut = 1;
		for (Map map : dataFromQuerySkillSdm) { 
			map.put("nourut", nourut);
			int nilai = (int) map.get("SDMSKILL_VALUE");
			
			if ((int) map.get("SKILLTYPE_ID") == 5 || (int) map.get("SKILLTYPE_ID") == 6) {
				map.put("SDMSKILL_VALUE", "");
			}else {
				if (nilai >= 1 && nilai < 7) {
					map.put("SDMSKILL_VALUE", "Beginner");
				}else if (nilai >= 7 && nilai < 9) {
					map.put("SDMSKILL_VALUE", "Intermediate");
				}else if (nilai >= 9) {
					map.put("SDMSKILL_VALUE", "Expert");
				}
			}
			listDataSkillSdm.add(map);
			nourut++;
		}
		
		List<Map> dataFromQueryEduc = Sdm.getDataEducation(sdmId);
		nourut = 1;
		for (Map map : dataFromQueryEduc) { 
			map.put("nourut", nourut);
			listDataEduc.add(map);
			nourut++;
		}
		
		List<Map> dataFromQueryCourse = Sdm.getDataCourse(sdmId);
		nourut = 1;
		for (Map map : dataFromQueryCourse) {
			map.put("nourut", nourut);
			listDataCourse.add(map);
			nourut++;
		}
		
		List<Map> dataFromQueryEmployment = Sdm.getDataEmployment(sdmId);
		nourut = 1;
		for (Map map : dataFromQueryEmployment) { 
			map.put("nourut", nourut);
			listDataEmployment.add(map);
			nourut++;
		}
		
		List<Map> dataFromQueryProject = Sdm.getDataProject(sdmId);
		nourut = 1;
		for (Map map : dataFromQueryProject) { 
			map.put("nourut", nourut);
			listDataProject.add(map);
			nourut++;
		}
		
		Map<String , Object> dataTampil  = new HashMap<>();
		dataTampil.put("data", listData);
		dataTampil.put("dataLanguage", listDataLanguage);
		dataTampil.put("dataProfiling", listDataProfiling);
		dataTampil.put("dataSkillSdm", listDataSkillSdm);
		dataTampil.put("dataEduc", listDataEduc);
		dataTampil.put("dataCourse", listDataCourse);
		dataTampil.put("dataEmployment", listDataEmployment);
		dataTampil.put("dataProject", listDataProject);
		
		System.out.println(JsonHelper.toJsonString(dataTampil));
		return dataTampil;
	}
		public String getConvertBulan(String now) {
			String bulan[] = {"Januari","Februari","Maret","April","Mei","Juni","Juli","Agustus","September","Oktober","Nopember","Desember"};
			String tanggal = now.substring(8,10);
			String bln = now.substring(5,7);
			String tahun = now.substring(0,4);
			String hasil =bulan[Integer.parseInt(bln)-1]+" "+tanggal+", "+tahun;
			System.out.println("cek : " + hasil);
			return hasil;
		}

}