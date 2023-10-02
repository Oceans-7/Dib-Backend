package com.oceans7.dib.domain.weather.dto;

import lombok.Getter;

@Getter
public enum ObsCode {

    SO_0732("SO_0732", "남애항", 128.788, 37.944, 90, 136),    //현남면
    SO_0733("SO_0733", "강릉항", 128.951, 37.772, 93, 132),    //송정동
    SO_0734("SO_0734", "궁촌항", 129.27, 37.327, 99, 124),     //근덕면
    SO_0735("SO_0735", "죽변항", 129.423, 37.054, 102, 117),   //죽변면
    SO_0736("SO_0736", "축산항", 129.448, 36.509, 103, 105),   //축반항
    SO_0737("SO_0737", "강구항", 129.391, 36.358, 102, 102),   //강구면
    SO_0555("SO_0555", "서망항", 126.134, 34.366, 46, 58),     //임회면
    DT_0054("DT_0054", "진해", 128.643, 35.147, 71, 75),      //진해구
    SO_0739("SO_0739", "도장항", 127.011, 34.367, 62, 57),     //금일읍
    SO_0740("SO_0740", "보옥항", 126.513, 34.129, 53, 53),     //보길면
    SO_0699("SO_0699", "천리포항", 126.146, 36.803, 46, 109),   //소원면
    SO_0562("SO_0562", "승봉도", 126.29, 37.169, 48, 120),     //자월면
    SO_0573("SO_0573", "양포항", 129.527, 35.881, 105, 92),    //장기면
    SO_0581("SO_0581", "강양항", 129.344, 35.39, 102, 81),     //온산읍
    SO_0571("SO_0571", "거제외포", 128.718, 34.939, 91, 71),    //장목면
    SO_0578("SO_0578", "소매물도", 128.548, 34.621, 88, 66),    //한산면
    SO_0567("SO_0567", "쉬미항", 126.183, 34.504, 48, 59),     //진도읍
    SO_0576("SO_0576", "화봉리", 126.256, 34.661, 49, 64),     //화원면
    SO_0564("SO_0564", "국화도", 126.56, 37.06, 57, 116),      //우정읍
    SO_0563("SO_0563", "울도", 125.995, 37.035, 46, 119),     //덕적면
    SO_0706("SO_0706", "청산도", 126.856, 34.18, 61, 136),     //청산면
    SO_0708("SO_0708", "안도항", 127.797, 34.479, 75, 60),     //남면
    SO_0712("SO_0712", "능양항", 128.245, 34.812, 83, 68),     //사량면
    SO_0701("SO_0701", "홍도항", 125.195, 34.681, 33, 64),     //흑산면
    SO_0702("SO_0702", "진도옥도", 126.018, 34.35, 44, 55),     //조도면
    SO_0703("SO_0703", "땅끝항", 126.531, 34.298, 52, 57),     //송지면
    SO_0704("SO_0704", "소안항", 126.631, 34.15, 55, 53),      //소안면
    DT_0040("DT_0040", "독도", 131.867, 37.238, 144, 123),     //독도
    DT_0002("DT_0002", "평택", 126.822, 36.966, 56, 112),     //신평면
    DT_0003("DT_0003", "영광", 126.42, 35.426, 51, 79),       //홍농읍
    DT_0004("DT_0004", "제주", 126.543, 33.527, 53, 38),      //건입동
    DT_0005("DT_0005", "부산", 129.035, 35.096, 97, 74),      //중앙동
    DT_0006("DT_0006", "묵호", 129.116, 37.55, 96, 127),      //발한동
    DT_0007("DT_0007", "목포", 126.375, 34.779, 50, 66),      //산정동
    DT_0008("DT_0008", "안산", 126.647, 37.192, 53, 120),     //대부동
    DT_0010("DT_0010", "서귀포", 126.561, 33.24, 53, 32),      //천지동
    DT_0011("DT_0011", "후포", 129.453, 36.677, 103, 109),     //후포면
    DT_0012("DT_0012", "속초", 128.594, 38.207, 87, 141),     //동명동
    DT_0013("DT_0013", "울릉도", 130.913, 37.491, 127, 127),   //울릉읍
    DT_0016("DT_0016", "여수", 127.765, 34.747, 74, 63),      //돌산읍
    DT_0017("DT_0017", "대산", 126.352, 37.007, 51, 113),     //대산읍
    DT_0018("DT_0018", "군산", 126.563, 35.975, 55, 92),      //소룡동
    DT_0021("DT_0021", "추자도", 126.3, 33.9, 48, 48),         //추자면
    DT_0023("DT_0023", "모슬포", 126.251, 33.214, 48, 32),     //대정읍
    DT_0028("DT_0028", "진도", 126.308, 34.377, 48, 58),      //의신면
    DT_0032("DT_0032", "강화대교", 126.522, 37.731, 53, 130),   //월곶면
    DT_0036("DT_0036", "대청도", 124.718, 37.825, 21, 132),    //대청면
    SO_0553("SO_0553", "해운대", 129.191, 35.16, 100, 75),     //중제2동
    SO_0540("SO_0540", "호산항", 129.342, 37.176, 101, 119),   //원덕읍
    DT_0020("DT_0020", "울산", 129.387, 35.501, 102, 83),     //장생포동
    DT_0022("DT_0022", "성산포", 126.927, 33.474, 60, 37),     //성산읍
    DT_0024("DT_0024", "장항", 126.687, 36.006, 53, 93),      //장항읍
    DT_0026("DT_0026", "고흥발포", 127.342, 34.481, 67, 60),    //도화면
    DT_0027("DT_0027", "완도", 126.759, 34.315, 57, 56),      //완도읍
    DT_0029("DT_0029", "거제도", 128.699, 34.801, 92, 68),     //일운면
    DT_0031("DT_0031", "거문도", 127.308, 34.028, 84, 70),     //삼산면
    DT_0035("DT_0035", "흑산도", 125.435, 34.684, 33, 64),     //흑산면
    DT_0044("DT_0044", "영종대교", 126.584, 37.545, 53, 125),   //원창동
    DT_0047("DT_0047", "도농탄", 126.274, 33.158, 48, 32),     //대정읍
    DT_0050("DT_0050", "태안", 126.238, 36.913, 48, 110),     //원북면
    DT_0048("DT_0048", "속초등표", 128.613, 38.199, 87, 141),   //청호동
    DT_0051("DT_0051", "서천마량", 126.495, 36.128, 53, 96),    //서면
    SO_0549("SO_0549", "초도", 127.245, 34.24, 84, 70),       //삼산면
    DT_0049("DT_0049", "광양", 127.754, 34.903, 74, 70),      //금호동
    DT_0056("DT_0056", "부산항신항", 128.786, 35.077, 93, 73),   //진해구
    DT_0057("DT_0057", "동해항", 129.143, 37.494, 97, 126),    //송정동
    SO_0538("SO_0538", "안마도", 126.016, 35.345, 45, 75),     //낙월면
    SO_0539("SO_0539", "강화외포", 126.372, 37.7, 50, 130),     //내가면
    DT_0058("DT_0058", "경인항", 126.601, 37.56, 54, 126),     //청라동
    SO_0554("SO_0554", "영종왕산", 126.358, 37.458, 50, 124),   //용유동
    SO_0326("SO_0326", "미조항", 128.048, 34.706, 80, 65),     //미조면
    IE_0060("IE_0060", "이어도", 125.182, 32.122, 28, 8),      //이어도
    DT_0038("DT_0038", "굴업도", 125.995, 37.194, 46, 119),    //덕적면
    DT_0025("DT_0025", "보령", 126.486, 36.406, 52, 102),     //오천면
    DT_0001("DT_0001", "인천", 126.592, 37.451, 53, 124),     //연안동
    DT_0052("DT_0052", "인천송도", 126.586, 37.338, 54, 123),   //연수구
    DT_0014("DT_0014", "통영", 128.434, 34.827, 87, 68),      //용남면
    DT_0037("DT_0037", "어청도", 125.984, 36.117, 43, 95),     //옥도면
    DT_0046("DT_0046", "쌍정초", 130.939, 37.556, 127, 129),   //북면
    DT_0039("DT_0039", "왕돌초", 129.732, 36.719, 103, 111),   //기성면
    DT_0041("DT_0041", "복사초", 126.168, 34.098, 44, 55),     //조도면
    DT_0042("DT_0042", "교본초", 128.306, 34.704, 84, 63),     //욕지면
    DT_0043("DT_0043", "영흥도", 126.428, 37.238, 51, 120),    //영흥면
    DT_0061("DT_0061", "삼천포", 128.069, 34.924, 80, 70),     //서금동
    SO_0537("SO_0537", "벽파진", 126.346, 34.539, 49, 60),     //고군면
    SO_0536("SO_0536", "덕적도", 126.157, 37.227, 46, 119),    //덕적면
    SO_0547("SO_0547", "말도", 126.318, 35.855, 49, 95),      //옥도면
    SO_0550("SO_0550", "나로도", 127.453, 34.463, 69, 59),     //봉래면
    SO_0705("SO_0705", "마량항", 126.821, 34.448, 58, 59),     //마량면
    SO_0707("SO_0707", "시산항", 127.261, 34.394, 64, 61),     //도양읍
    SO_0709("SO_0709", "두문포", 127.797, 34.643, 74, 63),     //돌산읍
    SO_0710("SO_0710", "봉우항", 127.927, 34.932, 77, 69),     //설천면
    SO_0711("SO_0711", "창선도", 128.019, 34.84, 79, 68),      //창선면
    SO_0700("SO_0700", "호도", 126.264, 36.303, 52, 102),     //오천면
    DT_0059("DT_0059", "백령도", 124.736, 37.955, 21, 135),    //백령면
    DT_0060("DT_0060", "연평도", 125.714, 37.657, 38, 129),    //연평면
    SO_0551("SO_0551", "여서도", 126.923, 33.988, 59, 53),     //청산면
    DT_0901("DT_0901", "포항_구", 129.374, 36.051, 102, 95),   //포항시북구
    SO_0552("SO_0552", "고현항", 128.622, 34.901, 91, 70),     //연초면
    IE_0062("IE_0062", "옹진소청초", 124.738, 37.423, 21, 132),  //대청면
    IE_0061("IE_0061", "신안가거초", 124.592, 33.941, 33, 64),   //흑산면
    SO_0543("SO_0543", "서거차도", 125.917, 34.253, 44, 55),    //조도면
    SO_0548("SO_0548", "우이도", 125.856, 34.62, 42, 64),      //도초면
    SO_0572("SO_0572", "읍천항", 129.475, 35.69, 104, 87),     //양남면
    SO_0569("SO_0569", "남포항", 128.321, 34.957, 85, 71),     //고성읍
    SO_0570("SO_0570", "광암항", 128.498, 35.102, 88, 74),     //진동면
    SO_0568("SO_0568", "백야도", 127.632, 34.624, 73, 63),     //화정면
    SO_0577("SO_0577", "가거도", 125.128, 34.05, 33, 64),      //흑산면
    SO_0566("SO_0566", "송공항", 126.225, 34.848, 49, 68),     //압해읍
    SO_0565("SO_0565", "향화도항", 126.359, 35.167, 50, 75),    //염산면
    SO_0574("SO_0574", "백사장항", 126.315, 36.586, 49, 104),   //안면읍
    SO_0731("SO_0731", "대진항", 128.426, 38.501, 84, 147),    //현내면
    SO_1251("SO_1251", "낙월도", 126.145, 35.2, 45, 75),       //낙월면
    SO_1252("SO_1252", "외연도항", 126.081, 36.225, 52, 102),   //오천면
    SO_0757("SO_0757", "안남리", 127.264, 34.73, 66, 66),      //대서면
    SO_0755("SO_0755", "원동항", 126.648, 34.393, 55, 57),     //군외면
    SO_0754("SO_0754", "평호리", 126.455, 34.448, 52, 60),     //화산면
    SO_1256("SO_1256", "어류정항", 126.342, 37.643, 49, 130),   //삼산면
    DT_0064("DT_0064", "교동대교", 126.339, 37.789, 48, 131),   //교동면
    SO_1249("SO_1249", "독거도", 126.178, 34.256, 44, 55),     //조도면
    SO_1250("SO_1250", "평도", 127.447, 34.245, 67, 50),      //삼산면
    SO_1253("SO_1253", "상왕등도", 126.11, 35.658, 48, 84),     //위도면
    SO_1254("SO_1254", "만재도", 125.472, 34.21, 33, 64),      //흑산면
    SO_1248("SO_1248", "신안옥도", 126.064, 34.683, 44, 62),    //하의면
    SO_0759("SO_0759", "장문리", 128.424, 34.873, 87, 68),     //용남면
    DT_0068("DT_0068", "위도", 126.301, 35.618, 48, 84),      //위도면
    SO_0760("SO_0760", "오산항", 129.416, 36.888, 102, 114),   //매화면
    SO_0753("SO_0753", "하의도웅곡", 126.038, 34.608, 44, 62),   //하의면
    SO_0631("SO_0631", "암태도", 126.071, 34.853, 45, 67),     //암태면
    SO_0752("SO_0752", "검산항", 126.107, 35.0, 46, 71),       //증도면
    SO_0761("SO_0761", "녹동항", 127.134, 34.527, 64, 61),     //도양읍
    DT_0067("DT_0067", "안흥", 126.129, 36.674, 47, 108),     //근흥면
    SO_1255("SO_1255", "상태도", 125.285, 34.435, 33, 64),     //흑산면
    SO_0758("SO_0758", "달천도", 127.563, 34.761, 72, 66),     //소라면
    SO_0756("SO_0756", "사초항", 126.761, 34.47, 56, 60),      //신전면
    DT_0063("DT_0063", "가덕도", 128.81, 35.024, 93, 73),      //강서구
    DT_0062("DT_0062", "마산", 128.576, 35.197, 89, 76),      //창원시마산합포구
    SO_1257("SO_1257", "강화하리", 126.286, 37.728, 49, 130),   //삼산면
    DT_0092("DT_0092", "여호항", 127.469, 34.661, 68, 63),     //점암면
    SO_1258("SO_1258", "잠진도", 126.415, 37.415, 50, 124),    //용유동
    SO_1259("SO_1259", "자월도", 126.318, 37.243, 48, 120),    //자월면
    SO_1260("SO_1260", "방포항", 126.325, 36.502, 49, 104),    //안면읍
    SO_1261("SO_1261", "무창포항", 126.534, 36.249, 54, 98),    //웅천읍
    SO_1262("SO_1262", "격포항", 126.463, 35.62, 52, 85),      //변산면
    SO_1263("SO_1263", "구시포항", 126.425, 35.447, 52, 80),    //상하면
    SO_1264("SO_1264", "계마항", 126.401, 35.39, 51, 79),      //홍농읍
    SO_1265("SO_1265", "송이도", 126.15, 35.271, 45, 75),      //낙월면
    SO_1266("SO_1266", "남열항", 127.48, 34.576, 69, 62),      //영남면
    SO_1267("SO_1267", "구룡포항", 129.555, 35.99, 105, 94),    //구룡포읍
    ;

    private String value;

    private String name;

    private double x;

    private double y;

    private int nx;

    private int ny;

    ObsCode(String value, String name, double x, double y, int nx, int ny) {
        this.value = value;
        this.name = name;
        this.x = x;
        this.y = y;
        this.nx = nx;
        this.ny = ny;
    }
}