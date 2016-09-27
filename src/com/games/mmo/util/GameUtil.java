package com.games.mmo.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.jdbc.support.rowset.SqlRowSet;

import com.games.backend.vo.UserSummaryVo;
import com.games.mmo.cache.GlobalCache;
import com.games.mmo.po.GlobalPo;
import com.games.mmo.type.RoleType;
import com.storm.lib.bean.CheckcCircleBean;
import com.storm.lib.component.entity.BaseDAO;
import com.storm.lib.type.BaseStormSystemType;
import com.storm.lib.util.ExceptionUtil;
import com.storm.lib.util.FileUtil;
import com.storm.lib.util.PrintUtil;
import com.storm.lib.util.RandomUtil;
import com.storm.lib.util.StringUtil;

public class GameUtil {
	
//	public static String[] tiFilterWord = new String[]{"fuck","shit","fuck you","cặc","lồn","lìn","âm đạo","bitch","âm vật","dương vật","bastard","butthead","butthole","buồi","fosaon","admin","gamemaster","gm","GM","bác hồ","bác-hồ","bác_hồ","báchồ","bac ho","bacho","Bac Ho","BacHo","bÁc hồ","Bác Hồ","BácHồ","Địt Mẹ Mày","Địt","DCM","DKM","dcm","dkm","cộng sản","Cộng sản","cong san","Cong San","Cộng Sản","cong san","Cong San","congsan","Cong_San","cong_san","cộng hòa","Cộng Hòa","cụ hồ","Cụ Hồ","hiếp","hiếp dâm","cưỡng hiếp","Chính Phủ","Chính Trị","chính phủ","chính trị","chủ tịch","Chủ Tịch","Chu Tich","địt con mẹ","địt mẹ","dm","DM","Đụ Má","Đụ Mẹ","Du Ma","Du Me","đụ má","đụ mẹ","du ma","du me","ĐCM","đĩ","đéo mẹ","Dis","dis","địt cụ","dis cu","địt_mẹ_mày","đụ_mẹ_mày","Hồ Chí Minh","hồ chí minh","Hồ_Chí_Minh","hồ_chí_minh","Ho Chi Minh","ho chi minh","Ho_Chi_Minh","ho_chi_minh","HoChiMinh","hochiminh","Hồ Chủ Tịch","hồ chủ tịch","mẹ kiếp","me kiep","Nông Đức Mạnh","nông đức mạnh","NongDucManh","nongducmanh","ngụy","Nguyễn Minh Triết","nguyễn minh triết","NguyenMinhTriet","nguyenminhtriet","Nguyễn Tấn Dũng","nguyễn tấn dũng","NguyenTanDung","nguyentandung","Hoàng Trung Hải","hoàng trung hải","hoangtrunghai","HoangTrungHai","Nguyễn Thiện Nhân","nguyễn thiện nhân","NguyenThienNhan","nguyenthiennhan","Nguyễn Xuân Phúc","nguyễn xuân phúc","nguyenxuanphuc","NguyenXuanPhuc","Vũ  Văn Ninh","vũ  văn ninh","VuVanNinh","Vu Van Ninh","Phùng Quang Thanh","phùng quang thanh","PhungQuangThanh","phungquangthanh","nhà nước","nha nuoc","phản động","phản quốc","sex","tin lành","Tin Lành","Tin_Lanh","tin_lanh","tôn giáo","ton giao","Tư Sản","tư sản","Tu San","tu san","Tư Bản","tư bản","Tu Ban","tu ban","Thiên Chúa","thiên chúa","Thien Chua","thien chua","thủ tướng","thu tuong","Trần Đức Lương","trần đức lương","Tran Duc Luong","tran duc luong","TranDucLuong","tranducluong","Võ Nguyên Giáp","võ nguyên giáp","Vo Nguyen Giap","vo nguyen giap","VoNguyenGiap","vonguyengiap","Việt Cộng","việt cộng","Viet Cong","viet cong","VietCong","vietcong","VKL","vkl","VL","vl","Tây Sa","Tay Sa","tây sa","tay sa","TâySa","TaySa","tâysa","taysa","Nam Sa","nam sa","NamSa","namsa","phản động","phản quốc","phan quoc","phan dong","32663","lìn bà mày","lin ba may","lồn bà mày","lon ba may","lìn mẹ mày","lin me may","lồn mẹ mày","lon me may","Địt Mẹ","địt mẹ","Dit Me","dit me","làm tình","lam tinh","FU","ăn cặc","bú cặc","F.U.C.K","DIT","Đis","Đit","địt cụ","địt mẹ mày","địt_con_mẹ_mày","địtconmẹ","địt-con-mẹ","địtmẹmày","địt-mẹ-mày","Di.t","di.t","dI.t","DI.t","DI.T","đi.t","Đi.t","ĐI.T","ĐịT","ĐỊT","diS","đis","Đis","Dit","Đit","DIT","ĐIT","ĐM","dmm","Đmm","đĩ_chó","địt cụ","đồ đĩ","đụ_má","đụ_mẹ","đụ_mẹ_mày","đụ_nhau","đụmá","đụ-mẹ","đụmẹmày","đụ-mẹ-mày","fuckyou","Fuckyou","hồ_chí_minh","hồ_chủ_tịch","hochiminh","hồchíminh","hồ-chủ-tịch","LaoLon","LáoLồn","hochutich","hồchủtịch","lìn bà mày","lìnbà","lìnchị","nông_đức_mạnh","nguyễn sinh cung","nguyễn tất thành","nguyen_minh_triet","nguyễn_minh_triết","Nguyen_tan_dung","Nguyễn_tấn_dũng","nguyễn_tất_thành","nguyenminhtriet","nguyễnminhtriết","Nguyentandung","nguyễntấndũng","nguyentatthanh","nhà-nước","nhanuoc","nhànước","phò cái","phò đực","phò_cái","sperm","tư_bản","tư_sản","thủ tướng","thu tuong","thu_tuong","thu-tuong","Thủ Tướng","Thu Tuong","Thu_Tuong","Thu-Tuong","vãi lồn","vailon","vãilồn","vai_lon","vai-lon","vina","Việt Cộng","việt_cộng","vietcong","việt-cộng","Tây*Sa","Tây!Sa","Tay!Sa","Tây@Sa","Tay*Sa","Tay@Sa","Tây#Sa","Tay#Sa","Tây$Sa","Tay$Sa","Tây%Sa","ăn lìn","Tây(Sa","Tay(Sa","Tây)Sa","Tay)Sa","Tây-Sa","Tay-Sa","Tây+Sa","Tay+Sa","Tây{Sa","Tay{Sa","Tây}Sa","an kac","an buoi","Tây'Sa","Tay'Sa","nguyễn_sinh_cung","nam sa","namsa","tây sa","buoi`","Buoi`","bu0`i","buỒi","BuỒi","b4c hỒ","b4c Hồ","bÁc Hồ","bÁc hỒ","Vật Hi Sinh","bac h0","Bac H0","cái_lồn","cai_lon","Cu_ho","cU_ho","cu_Ho","cu_hO","cụ hồ","cỤ hồ","cụ Hồ","cụ hỒ","c_a_c","C_a_c","c_A_c","C_A_c","C_A_C","C_a_c","c_A_c","c4c","C4c","C4C","cac.","Cac.","cAc.","CAC.","căc.","Căc.","CĂC.","cặc","Cặc","CẶc","CẶC","công_an","chính_phủ","chính_trị","chính-phủ","chinhtri","chínhtrị","chính-trị","deome","d1t","D1t","D1T","dit","Di~","DI~","di~","địt_mẹ","địt-mẹ","dit-me","đit_me","Lồn","lồn","Lon","lon","lOn","LoN","loN","LoN","lOn","tĩn","Tĩn","chó má","chó chết","tiên sư bố","NamSa","Nam Sa","Nam_Sa","Nam*Sa","Nam!Sa","Nam@Sa","Nam#Sa","Nam.Sa","Nam?Sa","Nam/Sa","TamSa","Tam Sa","Tam_Sa","Tam*Sa","Tam.Sa","Tam'Sa","Hoàng Sa","Hoang Sa","HoàngSa","Hoàng_Sa","Hoàng.Sa","Hoàng!Sa","Hoàng@Sa","Hoàng#Sa","HoangSa","HoangSA","HOANGSA","H0angSa","Trường Sa","TrườngSa","trường sa","hoàng sa","h0ang sa","h0àng sa","phan","PHAN","pHan","phal","PHAL","ph4n","trường!sa","trường@sa","trường#sa","trường.sa","Trường!sa","Trường@sa","trường#Sa","truongsa","truong sa","TRUONG SA","TRU0NG SA","tru0ng sa","phaN","PHan","PHaN","PhAn","pHaN","phAN","trường_sa","truong_sa","Trương Tấn Sang","TRƯƠNG TẤN SANG","Trương_Tấn_Sang","TRƯƠNG_TẤN_SANG","Trương_TẤN_sang","TrươngTấnSang","TRƯƠNGTẤNSANG","Nguyễn Phú Trọng","NGUYỄN PHÚ TRỌNG","Nguyễn_Phú_Trọng","NGUYỄN_PHÚ_TRỌNG","trương tấn sang","trương_tấn_sang","nguyễn phú trọng","nguyễn_phú_trọng","NGUYỄN phú Trọng","nguyễn PHÚ trọng","nguyễn phú TRỌNG","TRƯƠNG tấn sang","trương TẤN sang","trương tấn SANG","nguyễn sinh hùng","NGUYỄN SINH HÙNG","nguyễn_sinh_hùng","NGUYỄN_SINH_HÙNG","NGUYỄN sinh hùng","nguyễn SINH hùng","nguyễn sinh HÙNG","NGUYỄN SINH hùng","nguyễn sinh hung","NGUYÊN SINH HUNG","Tr. Tấn Sang","Ng. Sinh Hùng","Tr. Tấn Sang","nguyễn sinh hùng","cẶC","cẶc","cặc","cặC","CẶC","CẶc","Cặc","CặC","âmđạo","âmĐạo","âMđạo","âmđẠo","âmđạO","âMĐạo","âMđẠo","âMđạO","âmĐẠo","âmĐạO","âmđẠO","âMĐẠọ","âMĐạO","âMđẠO","âmĐẠO","âMĐẠO","ÂMĐẠO","Âmđạo","ÂmĐạo","ÂMđạo","ÂmđẠo","ÂmđạO","ÂMĐạo","ÂMđẠo","ÂMđạO","ÂmĐẠo","ÂmĐạO","ÂmđẠO","ÂMĐẠọ","ÂMĐạO","ÂMđẠO","ÂmĐẠO","ÂMĐẠO","dươngvật","DươngVật","DƯƠNGVẬT","DươngvậT","Wingame","WinGame","WINGAME","wingame","HIẾP","HIẾp","HIếp","Hiếp","hiếp","hiếP","hiẾP","hIẾP","hIẾp","hIếP","HiếP","HiẾp","hIếp","hiẾp","điếm","ĐIẾM","Điếm","ĐIếm","ĐIẾm","ĐiẾm","ĐiếM","đIếm","đIẾm","đIẾM","ĐIếm","đIếM","điẾm","điẾM","dái","DÁI","Dái","DÁi","DáI","dÁi","dÁI","dáI","CỨT","CỨt","Cứt","cứt","cứT","cỨT","cỨt","CứT","buồi","BUỒI","Buồi","BUồi","BUỒi","BuỒi","BuồI","bUồi","bUỒi","bUỒI","BUồi","bUồI","buỒi","buỒI","LỒN","LồN","lỒn","lồN","LỒn","lỒN","Lồn","lồn","BÍM","BÍm","Bím","bím","bíM","bÍM","bÍm","Lìn","LÌN","LìN","LÌn","lÌN","lìN","lÌn","ĐỤ","Đụ","đỤ","d1t","D1t","D1T","dit","Di~","DI~","di~","đĩ","ĐĨ","Đĩ","đĨ","địt_mẹ","địt-mẹ","dit-me","đit_me","Lồn","lồn","Lon","lon","lOn","LoN","loN","LoN","lOn","tĩn","Tĩn","báchồ","bác-hồ","bác_hồ","báchồ","bac ho","bacho","Bac Ho","BacHo","bÁchồ","BácHồ","BácHồ","ĐịtMẹMày","Địt","DCM","DKM","dcm","dkm","cộngsản","Cộngsản","congsan","CongSan","CộngSản","congsan","CongSan","congsan","Cong_San","cong_san","cộnghòa","CộngHòa","cụhồ","mẹ","MẸ","Mẹ","mẸ","CụHồ","hiếp","hiếpdâm","cưỡnghiếp","Chính Phủ","ChínhTrị","chínhphủ","chínhtrị","chủtịch","ChủTịch","ChuTich","địtconmẹ","địtmẹ","dm","DM","ĐụMá","ĐụMẹ","DuMa","DuMe","đụmá","đụmẹ","duma","dume","ĐCM","đĩ","đéomẹ","Dis","dis","địtcụ","ĐỊTCỤ","ĐịtCụ","DisCu","DIScu","DISCU","discu","địt_mẹ_mày","đụ_mẹ_mày","HồChíMinh","hồchíminh","Hồ_Chí_Minh","hồ_chí_minh","HoChiMinh","hochiminh","Ho_Chi_Minh","ho_chi_minh","HoChiMinh","hochiminh","HồChủTịch","hồchủtịch","Hồchủtịch","HỒchủTỊCH","HồCHỦtịch","hồchủTỊCH","HỒCHÍMINH","mẹkiếp","mekiep","NôngĐứcMạnh","nôngđứcmạnh","NongDucManh","nongducmanh","ngụy","NguyễnMinhTriết","nguyễnminhtriết","NguyenMinhTriet","nguyenminhtriet","NguyễnTấnDũng","nguyễntấndũng","NguyenTanDung","nguyentandung","Hoàng Trung Hải","hoàng trung hải","hoangtrunghai","HoangTrungHai","Nguyễn Thiện Nhân","nguyễn thiện nhân","NguyenThienNhan","nguyenthiennhan","Nguyễn Xuân Phúc","nguyễn xuân phúc","nguyenxuanphuc","NguyenXuanPhuc","Vũ  Văn Ninh","vũ  văn ninh","VuVanNinh","Vu Van Ninh","Phùng Quang Thanh","phùng quang thanh","PhungQuangThanh","phungquangthanh","nhànước","nhanuoc","phảnđộng","PhảnĐộng","Vãi","vãi","vÃI","phảnquốc","Di.t","di.t","dI.t","DI.t","DI.T","đi.t","Đi.t","ĐI.T","ĐịT","ĐỊT","diS","đis","Đis","Dit","Đit","DIT","Địt","ĐỊt","ĐỊT","đỊt","đỊT","ĐIT","ĐM","dmm","Đmm","Địt","DCM","DKM","dcm","dkm","cặc","lồn","lìn","âmđạo","bitch","âmvật","dươngvật","ÂMVẬT","ÂmVật","ÂMVật","âmđạo","ÂMĐẠO","buồi","bác hồ","bác-hồ","bác_hồ","báchồ","bacho","bacho","BacHo","BacHo","bÁchồ","BÁcHồ","BácHồ","BÁC","BÁc","báC","BáC","ĐịtMẹMày","Địt","DCM","DKM","dcm","dkm","cặc","AdminLàEm","âm_vật","âm-đạo","amvat","âm-vật","báchồ","bác-hồ","BánTrinh","bede","BộĐộiCụHồ","bomay","bốmày","bome","bỏmẹ","bú","bựavãihàng","cặc","caibuoi","cáibuồi","cailon","cái-lồn","canhsat","cảnh-sát","cave","CaVe","CAve","CAVe","cAVe","cAVE","chamàychứ","chichxongsoc","chínhxongsốc","chimmemay","chim-mẹ-mày","chính_trị","chinhtri","chính-trị","chó_chết","chochet","chó-chết","chóđẻ","chơi_gái","choigai","chơi-gái","chủ_tịch","chủtịch","con_cặc","con_mẹ_mày","concạc","concặc","con-cặc","cộng_hoà","congan","công-an","cộnghoà","congsan","CỘNGSẢN","cộng-sản","conmemay","con-mẹ-mày","cức","cục_phân","cụcphân","cuccut","cụccứt","cucphan","cục-phân","CuEmTo","cưỡng_hiếp","cưỡng-dâm","cuonghiep","cưỡng-hiếp","dái","ĐạiThủDâmTặc","dâmđãng","dâm-tặc","damthue","đâm-thuê","đánh_bom","đánh-bom","danhloile","đàongũ","đátungdái","dcm","đéo_mẹ","deome","đéomẹ","đĩ","đĩ_chó","địt mẹ mày","địt_mẹ_mày","ditconme","địt-con-mẹ","ditmemay","địtmẹmày","địt-mẹ-mày","dm","đồi_trụy","ĐờiNhưConCặc","đồi-trụy","đụ_má","đụ_mẹ_mày","đụ_nhau","duma","đụmá","đụmẹ","dumemay","đụmẹmày","dương_vật","dương-vật","đút","EmĂnMáuLồn","GáiTrinhbánDâm","gay","giethet","hiepdam","hiếp-dâm","hiếpmày","hiếpxongbiến","hồ_chí_minh","hochiminh","hồ-chí-minh","hồchủtịch","hộtle","HỘTLE","HộtLe","kememay","kệ-mẹ-mày","làm_tình","làmtình","LaoLon","liếm","chómá","chóchết","chóđẻ","ChóMá","lieuchet","lìnbà","lìnchị","lồnmá","má_mày","mamay","má-mày","mẹ_kiếp","mê_tín","mekiep","mẹ-kiếp","mẹmày","mẹmày","mẹmàychứ","mod","mongdoc","MuốnLàmTình","nguyen_minh_triet","Nguyen_tan_dung","nguyen_tat_thanh","nguyenminhtriet","nguyễnminhtriết","Nguyentandung","nguyentatthanh","nhànước","NhìnCáiLồnMẹMày","nông_đức_mạnh","nước_tiểu","nuocdai","nước-đái","nước-tiểu","nút","phá trinh","phá_trinh","PhạmVănĐồng","phandong","phản-động","phảnquốc","phatgiao","phật-giáo","phệtchếtmày","phò_đực","phòcái","sóc_hàng","sóc_lọ","subochungmay","suchachungmay","tiensumemay","tín_ngưỡng","tinhkhí","tinhtrùng","tinh_trùng","tinlành","tochachungmay","tongiao","tôn-giáo","tư_bản","tư_tưởng","phan","PHAN","phal","PHAL","ph4n","phaN","Phan","PHaN","PhAn","pHaN","phAN","HoàngSa","HoangSa","HoàngSa","trường_sa","truong_sa","TrươngTấnSang","TRƯƠNGTẤNSANG","TrươngTấnSang","TRƯƠNGTẤNSANG","NguyễnPhúTrọng","NGUYỄNPHÚTRỌNG","trươngtấnsang","trương_tấn_sang","nguyễn phú trọng","nguyễn_phú_trọng","NGUYỄNphúTrọng","nguyễnPHÚtrọng","nguyễnphTRỌNG","TRƯƠNGtấnsang","trươngTẤNsang","trươngtấnSANG","nguyễnsinhhùng","NGUYỄNSINHHÙNG","NGUYỄNsinhhùng","nguyễnSINHhùng","nguyễnsinhHÙNG","NGUYỄNSINHhùng","nguyễnsinhhung","NGUYÊNSINHHUNG","nguyễnsinhhùng","HoangSa","HoangSA","HOANGSA","H0angSa","Trường Sa","TrườngSa","trườngsa","hoàngsa","h0angsa","h0àngsa","NGUYỄNsinhhùng","nguyễnSINHhùng","nguyễnsinhHÙNG","NGUYỄNSINHhùng","nguyễnsinhhung","NGUYÊNSINHHUNG"};
	public static String createRandomRobotName(Integer sex){
		if(sex == null){
			sex = 1;
		}
		CheckcCircleBean checkcCircleBean = new CheckcCircleBean(1000);
		StringBuffer sb = new StringBuffer();
		while(true)
		{
			checkcCircleBean.count();
			if(sex == 1)
			{
				sb.append(RandomUtil.randomStringByArray(RoleType.BOYNAMESTR));
			}
			else if(sex == 0){
				sb.append(RandomUtil.randomStringByArray(RoleType.GIRLNAMESTR));
			}
			sb.append(RandomUtil.randomStringByArray(RoleType.SPACERSSTR));
			sb.append(RandomUtil.randomStringByArray(RoleType.LASTNAMESTR));
			
//			PrintUtil.print(sb.toString() + "|| " + sb.toString().length());
//			if(sb.toString().length() <= 14){
				return sb.toString();
//			}
		}
	}
	public static boolean isSameDay(Date day1, Date day2) {
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	    String ds1 = sdf.format(day1);
	    String ds2 = sdf.format(day2);
	    if (ds1.equals(ds2)) {
	        return true;
	    } else {
	        return false;
	    }
	}
	public static double sum(int n){ //  ∑ 
		double sum=0;
		for(int i=1;i<=n;i++){
			sum +=1/(double)i;
		}
		return sum;
	}
	
	/**
	 * 获取两个数字相差值
	 * @return
	 */
	public static int getDiffer(int x1, int x2)
	{
		return (x1 >> 31 != x2 >> 31)?Math.abs(x1)+Math.abs(x2):Math.abs(Math.abs(x1)-Math.abs(x2));
	}
	
	
	/**
	 * 获得属性名字
	 * @return
	 */
	public static String getAtbDescripeByAtbType(int atbType,int atbValue )
	{
		String name="";
		int [] atbTypes={1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25,26,27,28,29,30,31,32,33};
		String[] atbNames={GlobalCache.fetchLanguageMap("key270"),
										   GlobalCache.fetchLanguageMap("key271"),
										   GlobalCache.fetchLanguageMap("key272"),
										   GlobalCache.fetchLanguageMap("key273"),
										   GlobalCache.fetchLanguageMap("key274"),
										   GlobalCache.fetchLanguageMap("key275"),
										   GlobalCache.fetchLanguageMap("key276"),
										   GlobalCache.fetchLanguageMap("key277"),
										   GlobalCache.fetchLanguageMap("key278"),
										   GlobalCache.fetchLanguageMap("key279"),
										   GlobalCache.fetchLanguageMap("key280"),
										   GlobalCache.fetchLanguageMap("key281"),
										   GlobalCache.fetchLanguageMap("key282"),
										   GlobalCache.fetchLanguageMap("key283"),
										   GlobalCache.fetchLanguageMap("key284"),
										   GlobalCache.fetchLanguageMap("key285"),
										   GlobalCache.fetchLanguageMap("key286"),
										   GlobalCache.fetchLanguageMap("key287"),
										   GlobalCache.fetchLanguageMap("key288"),
										   GlobalCache.fetchLanguageMap("key289"),
										   GlobalCache.fetchLanguageMap("key290"),
										   GlobalCache.fetchLanguageMap("key291"),
										   GlobalCache.fetchLanguageMap("key292"),
										   GlobalCache.fetchLanguageMap("key293"),
										   GlobalCache.fetchLanguageMap("key294"),
										   GlobalCache.fetchLanguageMap("key295"),
										   GlobalCache.fetchLanguageMap("key296"),
										   GlobalCache.fetchLanguageMap("key297"),
										   GlobalCache.fetchLanguageMap("key298"),
										   GlobalCache.fetchLanguageMap("key299"),
										   GlobalCache.fetchLanguageMap("key300"),
										   GlobalCache.fetchLanguageMap("key301"),
										   GlobalCache.fetchLanguageMap("key302")};
		
		for(int i=0;i<atbTypes.length;i++){
			if(atbTypes[i]==atbType){
				if(atbType==18||atbType>=25){
					name=atbNames[i]+atbValue/10d+"%";
				}else{
					name=atbNames[i]+atbValue;
				}
			}
		}
		return name;
	}
	
	public static void checkContianFiltedWord2(String str,boolean requestSystemCheck){
 		if(containsFilterWord(str)){
			ExceptionUtil.throwConfirmParamException("There are filter words:"+str);
		}
		if(requestSystemCheck){
			for(int i=0;i<filterStrings90.length;i++){
				if(str.contains(filterStrings90[i])){
					ExceptionUtil.throwConfirmParamException("There are filter words:"+str);
				}
			}		
			for(int i=0;i<filterStrings90.length;i++){
				if(str.contains(filterStrings90[i])){
					ExceptionUtil.throwConfirmParamException("There are filter words:"+str);
				}
			}
			//首写不能为数字
			
			 
		}
	}

	public static boolean containsFilterWord(String str) {
		for (String word : filterStrings90) {
			if (str.indexOf(word) != -1) {
				PrintUtil.print(str + "[ 有过滤词：]" + word);
				return true;
			}
		}
		return false;
	}	
	
	
	/**
	 * 
	 * 方法功能:判断是否有过滤词
	 * 更新时间:2011-12-23, 作者:johnny
	 */
	public static void checkContianFiltedWord(String str,boolean requestSystemCheck,String[] additionStrs){
 		if(containsFilterWord(str)){
			ExceptionUtil.throwConfirmParamException("1There are filter words:"+str);
		}
 		if(additionStrs!=null){
 			for(int i=0;i<additionStrs.length;i++){
 				if(str.contains(additionStrs[i])){
 					ExceptionUtil.throwConfirmParamException("2There are filter words:"+str);
 				}
 			}	
 		}

		if(requestSystemCheck){
			for(int i=0;i<filterStrings90.length;i++){
				if(str.contains(filterStrings90[i])){
					ExceptionUtil.throwConfirmParamException("3There are filter words:"+str);
				}
			}		
			for(int i=0;i<filterStrings90.length;i++){
				if(str.contains(filterStrings90[i])){
					ExceptionUtil.throwConfirmParamException("4There are filter words:"+str);
				}
			}
			//首写不能为数字
			
			if(StringUtil.isNumeric(str.substring(0,1))){
				ExceptionUtil.throwConfirmParamException("The first character can't Numbers:"+str);
			}
		}
	}	
	
	public static String[] filterStrings90;
	
	
	
	public static void reloadFilterString(){
		try {
			GlobalPo gp = (GlobalPo) GlobalPo.keyGlobalPoMap.get(GlobalPo.keyLanguage);
			List<String> list = new ArrayList<String>();
			File file = FileUtil.getUnderDataFile("filter_"+gp.getValueStr()+".txt");
			BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
			String tempStr=null;
			int line=1;
			while((tempStr=bufferedReader.readLine())!=null){
				String[] vals = StringUtil.split(tempStr,",");
				for (String string : vals) {
					if(string.equals("")){
						continue;
					}
					list.add(string);
				}
			}
			filterStrings90 = list.toArray(new String[list.size()]);
		} catch (IOException e) {
			ExceptionUtil.processException(e);
		}
	}

		
	/**
	 * @param startTime
	 * @param endTime
	 * @param filter
	 * @param device
	 * @param chargeStart
	 * @param chargeEnd
	 * @param roleId
	 * @param userIuid
	 * @param roleName
	 * @return
	 */
	public static List<UserSummaryVo> findUserSummary(Long startTime, Long endTime,
			Integer filter, Integer device, Integer chargeStart,
			Integer chargeEnd, Integer roleId, String userIuid, String roleName, Integer iCurrPage, String orderBy, Integer sort) {
		List<UserSummaryVo> userSummaryVos=new ArrayList<UserSummaryVo>();
//		String result="";
//    	result="【用户列表查询结果】</br>";
//    	result+="<table  border='1' style='width:90%'><tbody><tr><th>账号id</th><th>账号</th><th>渠道key</th><th>id</th><th>名字</th><th>新手</th><th>等级</th><th>经验</th><th>非绑金</th>	<th>绑金</th><th>非绑钻</th><th>绑钻</th><th>职业</th><th>竞技场排行</th><th>技能点</th><th>成就点</th><th>声望</th><th>公会荣誉</th><th>PK值</th><th>每日任务点数</th><th>魔魂</th><th>帮派名称</th><th>主线任务ID</th><th>主线任务名称</th><th>最近次登录时间</th><th>最近次登出时间</th><th>账号创建时间</th><th>角色创建时间</th><th>战力</th><th>总共充值</th><th>首充钻石</th><th>首充角色等级</th><th>首次消费描述</th><th>首次消费角色等级</th></tr></tbody>";
		StringBuffer sb=new StringBuffer();
		sb.append("select r.user_id,u.iuid,r.channel_key,r.id,r.name,r.newbie_step_group,r.lv,r.exp,r.gold,r.bind_gold,r.diamond,bind_diamond,r.career," +
				"r.arena_rank,r.skill_point,r.achieve_point,r.prestige,r.guild_honor,r.pk_value,r.daily_lively_task_finish_score,r.pet_soul,r.guild_name," +
				"r.main_task_id,r.main_task_name,r.last_login_time,r.last_logoff_time,r.user_created_time,r.create_time,r.battle_power,r.total_diamond_charged," +
				"r.first_charge_diamond,r.first_charge_role_lv,r.first_consume_desc,r.first_consume_role_lv,r.vip_lv,u.idfa,u.package_name,r.last_login_ip,u.device_id,u.server_id from ")
				.append(BaseStormSystemType.USER_DB_NAME).append(".u_po_role r left join ").append(BaseStormSystemType.USER_DB_NAME)
				.append(".u_po_user u on r.user_id=u.id where r.total_diamond_charged>=").append(chargeStart).append(" and r.total_diamond_charged<=").append(chargeEnd);
		if(filter==1){
			sb.append(" and r.user_created_time>="+startTime+" and r.user_created_time<="+endTime); 
		}
		if(filter==2){
			sb.append(" and r.create_time>="+startTime+" and r.create_time<="+endTime);
		}
		if(filter==3){
			sb.append(" and r.last_login_time>="+startTime+" and r.last_logoff_time<="+endTime);
		}
		if(roleId!=0){
			sb.append(" and r.id="+roleId);
		}
		if(StringUtil.isNotEmpty(roleName)){
			sb.append(" and r.name='"+roleName+"'");
		}
		if(StringUtil.isNotEmpty(userIuid)){
			sb.append(" and u.iuid='"+userIuid+"'");
		}
		if(StringUtil.isNotEmpty(orderBy)){
			if(sort.intValue() == 0){
				sb.append(" order by ").append(orderBy);				
			}else{
				sb.append(" order by ").append(orderBy).append(" desc");
			}
		}
		if (iCurrPage != -1) {//表示查某一页数据
			sb.append(" limit ").append(iCurrPage*50).append(", 50");
		}
		
		Map<String, UserSummaryVo> userIuidUserSummaryVoMap = new LinkedHashMap<String, UserSummaryVo>();
		SqlRowSet rs = BaseDAO.instance().jdbcTemplate.queryForRowSet(sb.toString());
		while (rs.next()) {
			UserSummaryVo userSummaryVo=new UserSummaryVo();
			userSummaryVo.user_id=rs.getInt("user_id");
			userSummaryVo.user_iuid=rs.getString("iuid");
			userSummaryVo.channel_key=rs.getString("channel_key");
			userSummaryVo.id=rs.getInt("id");
			userSummaryVo.name=rs.getString("name");
			userSummaryVo.newbie_step_group=rs.getInt("newbie_step_group");
			userSummaryVo.lv=rs.getInt("lv");
			userSummaryVo.exp=rs.getInt("exp");
			userSummaryVo.gold=rs.getInt("gold");
			userSummaryVo.bind_gold=rs.getInt("bind_gold");
			userSummaryVo.diamond=rs.getInt("diamond");
			userSummaryVo.bind_diamond=rs.getInt("bind_diamond");
			userSummaryVo.career=rs.getInt("career");
			userSummaryVo.arena_rank=rs.getInt("arena_rank");
			userSummaryVo.skill_point=rs.getInt("skill_point");
			userSummaryVo.achieve_point=rs.getInt("achieve_point");
			userSummaryVo.prestige=rs.getInt("prestige");
			userSummaryVo.guild_honor=rs.getInt("guild_honor");
			userSummaryVo.pk_value=rs.getInt("pk_value");
			userSummaryVo.daily_lively_task_finish_score=rs.getInt("daily_lively_task_finish_score");
			userSummaryVo.pet_soul=rs.getInt("pet_soul");
			userSummaryVo.guild_name=rs.getString("guild_name");
			userSummaryVo.main_task_id=rs.getInt("main_task_id");
			userSummaryVo.main_task_name=rs.getString("main_task_name");
			userSummaryVo.last_login_time=rs.getLong("last_login_time");
			userSummaryVo.last_logoff_time=rs.getLong("last_logoff_time");
			userSummaryVo.user_created_time=rs.getLong("user_created_time");
			userSummaryVo.create_time=rs.getLong("create_time");
			userSummaryVo.battle_power=rs.getInt("battle_power");
			
			
			
			userSummaryVo.total_diamond_charged=rs.getInt("total_diamond_charged");
			userSummaryVo.first_charge_diamond=rs.getInt("first_charge_diamond");
			userSummaryVo.first_charge_role_lv=rs.getInt("first_charge_role_lv");
			userSummaryVo.first_consume_desc=rs.getString("first_consume_desc");
			userSummaryVo.first_consume_role_lv=rs.getInt("first_consume_role_lv");
			userSummaryVo.vip_lv=rs.getInt("vip_lv");
			userSummaryVo.idfa=rs.getString("idfa");
			userSummaryVo.package_name=rs.getString("package_name");
			userSummaryVo.lastLoginIp=rs.getString("last_login_ip");
			userSummaryVo.deviceId=rs.getString("device_id");
			userSummaryVo.serverId=rs.getInt("server_id");
			
			if(!userIuidUserSummaryVoMap.containsKey(userSummaryVo.user_id.toString())){
				if(device==0){
					userIuidUserSummaryVoMap.put("r_"+userSummaryVo.id, userSummaryVo);
				}
				else{
					userIuidUserSummaryVoMap.put(userSummaryVo.user_id.toString(), userSummaryVo);
				}

			}
			else{
				UserSummaryVo oldUserSummaryVo=userIuidUserSummaryVoMap.get(userSummaryVo.user_id.toString());
				if(device==1){
					if(userSummaryVo.newbie_step_group>oldUserSummaryVo.newbie_step_group){
						userIuidUserSummaryVoMap.put(userSummaryVo.user_id.toString(), userSummaryVo);
					}
				}
				else if(device==2){
					if(userSummaryVo.lv>oldUserSummaryVo.lv){
						userIuidUserSummaryVoMap.put(userSummaryVo.user_id.toString(), userSummaryVo);
					}
					else if(userSummaryVo.lv==oldUserSummaryVo.lv.intValue()){
						if(userSummaryVo.exp>oldUserSummaryVo.exp.intValue()){
							userIuidUserSummaryVoMap.put(userSummaryVo.user_id.toString(), userSummaryVo);
						}
					}
				}
				else if(device==3){
					if(userSummaryVo.main_task_id>oldUserSummaryVo.main_task_id){
						userIuidUserSummaryVoMap.put(userSummaryVo.user_id.toString(), userSummaryVo);
					}
				}
				else if(device==0){
					
					userIuidUserSummaryVoMap.put("r_"+userSummaryVo.id, userSummaryVo);
					
				}				
			}
		}   
		
		Iterator iter = userIuidUserSummaryVoMap.entrySet().iterator();
		while (iter.hasNext()) {
			Map.Entry entry = (Map.Entry) iter.next();
			String key = (String)entry.getKey();
			UserSummaryVo userSummaryVo = (UserSummaryVo)entry.getValue();
//			result+=userSummaryVo.toString()+"</br>";
			userSummaryVos.add(userSummaryVo);
		}
		return userSummaryVos;
	}
	public static int buildBattlePower(List<List<Integer>> listEqpBatAttrExp) {
		int power=0;
		for(int i=0;i<listEqpBatAttrExp.size();i++){
			int atbId=listEqpBatAttrExp.get(i).get(0);
			int abtNum=listEqpBatAttrExp.get(i).get(1);
			if(atbId<=RoleType.batMeleeWarriorOrArcher.length){
				double powPar=RoleType.batMeleeWarriorOrArcher[atbId-1];
				power+=(int)(abtNum*powPar);
			}
		}
		return power;
	}
    	
}
