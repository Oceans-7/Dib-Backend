package com.oceans7.dib.domain.weather.dto;

import lombok.Getter;

@Getter
public enum ObsCode {

    // TODO 임시로 mapX, mapY 통일 해두었으면 각 관측소에 가장 가까운 격자 값으로 수정해야함
    SO_0732("SO_0732", "남애항", 37.944, 128.788, 99, 75),
    SO_0733("SO_0733", "강릉항", 37.772, 128.951, 99, 75),
    SO_0734("SO_0734", "궁촌항", 37.327, 129.27, 99, 75),
    SO_0735("SO_0735", "죽변항", 37.054, 129.423, 99, 75),
    SO_0736("SO_0736", "축산항", 36.509, 129.448, 99, 75),
    SO_0737("SO_0737", "강구항", 36.358, 129.391, 99, 75),
    SO_0555("SO_0555", "서망항", 34.366, 126.134, 99, 75),
    DT_0054("DT_0054", "진해", 35.147, 128.643, 99, 75),
    SO_0739("SO_0739", "도장항", 34.367, 127.011, 99, 75),
    SO_0740("SO_0740", "보옥항", 34.129, 126.513, 99, 75),
    SO_0699("SO_0699", "천리포항", 36.803, 126.146, 99, 75),
    SO_0562("SO_0562", "승봉도", 37.169, 126.29, 99, 75),
    SO_0573("SO_0573", "양포항", 35.881, 129.527, 99, 75),
    SO_0581("SO_0581", "강양항", 35.39, 129.344, 99, 75),
    SO_0571("SO_0571", "거제외포", 34.939, 128.718, 99, 75),
    SO_0578("SO_0578", "소매물도", 34.621, 128.548, 99, 75),
    SO_0567("SO_0567", "쉬미항", 34.504, 126.183, 99, 75),
    SO_0576("SO_0576", "화봉리", 34.661, 126.256, 99, 75),
    SO_0564("SO_0564", "국화도", 37.06, 126.56, 99, 75),
    SO_0563("SO_0563", "울도", 37.035, 125.995, 99, 75),
    SO_0706("SO_0706", "청산도", 34.18, 126.856, 99, 75),
    SO_0708("SO_0708", "안도항", 34.479, 127.797, 99, 75),
    SO_0712("SO_0712", "능양항", 34.812, 128.245, 99, 75),
    SO_0701("SO_0701", "홍도항", 34.681, 125.195, 99, 75),
    SO_0702("SO_0702", "진도옥도", 34.35, 126.018, 99, 75),
    SO_0703("SO_0703", "땅끝항", 34.298, 126.531, 99, 75),
    SO_0704("SO_0704", "소안항", 34.15, 126.631, 99, 75),
    DT_0040("DT_0040", "독도", 37.238, 131.867, 99, 75),
    DT_0002("DT_0002", "평택", 36.966, 126.822, 99, 75),
    DT_0003("DT_0003", "영광", 35.426, 126.42, 99, 75),
    DT_0004("DT_0004", "제주", 33.527, 126.543, 99, 75),
    DT_0005("DT_0005", "부산", 35.096, 129.035, 99, 75),
    DT_0006("DT_0006", "묵호", 37.55, 129.116, 99, 75),
    DT_0007("DT_0007", "목포", 34.779, 126.375, 99, 75),
    DT_0008("DT_0008", "안산", 37.192, 126.647, 99, 75),
    DT_0010("DT_0010", "서귀포", 33.24, 126.561, 99, 75),
    DT_0011("DT_0011", "후포", 36.677, 129.453, 99, 75),
    DT_0012("DT_0012", "속초", 38.207, 128.594, 99, 75),
    DT_0013("DT_0013", "울릉도", 37.491, 130.913, 99, 75),
    DT_0016("DT_0016", "여수", 34.747, 127.765, 99, 75),
    DT_0017("DT_0017", "대산", 37.007, 126.352, 99, 75),
    DT_0018("DT_0018", "군산", 35.975, 126.563, 99, 75),
    DT_0021("DT_0021", "추자도", 33.9 , 126.3, 99, 75),
    DT_0023("DT_0023", "모슬포", 33.214, 126.251, 99, 75),
    DT_0028("DT_0028", "진도", 34.377, 126.308, 99, 75),
    DT_0032("DT_0032", "강화대교", 37.731, 126.522, 99, 75),
    DT_0036("DT_0036", "대청도", 37.825, 124.718, 99, 75),
    SO_0553("SO_0553", "해운대", 35.16, 129.191, 99, 75),
    SO_0540("SO_0540", "호산항", 37.176, 129.342, 99, 75),
    DT_0020("DT_0020", "울산", 35.501, 129.387, 99, 75),
    DT_0022("DT_0022", "성산포", 33.474, 126.927, 99, 75),
    DT_0024("DT_0024", "장항", 36.006, 126.687, 99, 75),
    DT_0026("DT_0026", "고흥발포", 34.481, 127.342, 99, 75),
    DT_0027("DT_0027", "완도", 34.315, 126.759, 99, 75),
    DT_0029("DT_0029", "거제도", 34.801, 128.699, 99, 75),
    DT_0031("DT_0031", "거문도", 34.028, 127.308, 99, 75),
    DT_0035("DT_0035", "흑산도", 34.684, 125.435, 99, 75),
    DT_0044("DT_0044", "영종대교", 37.545, 126.584, 99, 75),
    DT_0047("DT_0047", "도농탄", 33.158, 126.274, 99, 75),
    DT_0050("DT_0050", "태안", 36.913, 126.238, 99, 75),
    DT_0048("DT_0048", "속초등표", 38.199, 128.613, 99, 75),
    DT_0051("DT_0051", "서천마량", 36.128, 126.495, 99, 75),
    SO_0549("SO_0549", "초도", 34.24, 127.245, 99, 75),
    DT_0049("DT_0049", "광양", 34.903, 127.754, 99, 75),
    DT_0056("DT_0056", "부산항신항", 35.077, 128.786, 99, 75),
    DT_0057("DT_0057", "동해항", 37.494, 129.143, 99, 75),
    SO_0538("SO_0538", "안마도", 35.345, 126.016, 99, 75),
    SO_0539("SO_0539", "강화외포", 37.7, 126.372, 99, 75),
    DT_0058("DT_0058", "경인항", 37.56, 126.601, 99, 75),
    SO_0554("SO_0554", "영종왕산", 37.458, 126.358, 99, 75),
    SO_0326("SO_0326", "미조항", 34.706, 128.048, 99, 75),
    IE_0060("IE_0060", "이어도", 32.122, 125.182, 99, 75),
    DT_0038("DT_0038", "굴업도", 37.194, 125.995, 99, 75),
    DT_0025("DT_0025", "보령", 36.406, 126.486, 99, 75),
    DT_0001("DT_0001", "인천", 37.451, 126.592, 99, 75),
    DT_0052("DT_0052", "인천송도", 37.338, 126.586, 99, 75),
    DT_0014("DT_0014", "통영", 34.827, 128.434, 99, 75),
    DT_0037("DT_0037", "어청도", 36.117, 125.984, 99, 75),
    DT_0046("DT_0046", "쌍정초", 37.556, 130.939, 99, 75),
    DT_0039("DT_0039", "왕돌초", 36.719, 129.732, 99, 75),
    DT_0041("DT_0041", "복사초", 34.098, 126.168, 99, 75),
    DT_0042("DT_0042", "교본초", 34.704, 128.306, 99, 75),
    DT_0043("DT_0043", "영흥도", 37.238, 126.428, 99, 75),
    DT_0061("DT_0061", "삼천포", 34.924, 128.069, 99, 75),
    SO_0537("SO_0537", "벽파진", 34.539, 126.346, 99, 75),
    SO_0536("SO_0536", "덕적도", 37.227, 126.157, 99, 75),
    SO_0547("SO_0547", "말도", 35.855, 126.318, 99, 75),
    SO_0550("SO_0550", "나로도", 34.463, 127.453, 99, 75),
    SO_0705("SO_0705", "마량항", 34.448, 126.821, 99, 75),
    SO_0707("SO_0707", "시산항", 34.394, 127.261, 99, 75),
    SO_0709("SO_0709", "두문포", 34.643, 127.797, 99, 75),
    SO_0710("SO_0710", "봉우항", 34.932, 127.927, 99, 75),
    SO_0711("SO_0711", "창선도", 34.84, 128.019, 99, 75),
    SO_0700("SO_0700", "호도", 36.303, 126.264, 99, 75),
    DT_0059("DT_0059", "백령도", 37.955, 124.736, 99, 75),
    DT_0060("DT_0060", "연평도", 37.657, 125.714, 99, 75),
    SO_0551("SO_0551", "여서도", 33.988, 126.923, 99, 75),
    DT_0901("DT_0901", "포항_구", 36.051, 129.374, 99, 75),
    SO_0552("SO_0552", "고현항", 34.901, 128.622, 99, 75),
    IE_0062("IE_0062", "옹진소청초", 37.423, 124.738, 99, 75),
    IE_0061("IE_0061", "신안가거초", 33.941, 124.592, 99, 75),
    SO_0543("SO_0543", "서거차도", 34.253, 125.917, 99, 75),
    SO_0548("SO_0548", "우이도", 34.62, 125.856, 99, 75),
    SO_0572("SO_0572", "읍천항", 35.69, 129.475, 99, 75),
    SO_0569("SO_0569", "남포항", 34.957, 128.321, 99, 75),
    SO_0570("SO_0570", "광암항", 35.102, 128.498, 99, 75),
    SO_0568("SO_0568", "백야도", 34.624, 127.632, 99, 75),
    SO_0577("SO_0577", "가거도", 34.05, 125.128, 99, 75),
    SO_0566("SO_0566", "송공항", 34.848, 126.225, 99, 75),
    SO_0565("SO_0565", "향화도항", 35.167, 126.359, 99, 75),
    SO_0574("SO_0574", "백사장항", 36.586, 126.315, 99, 75),
    SO_0731("SO_0731", "대진항", 38.501, 128.426, 99, 75),
    SO_1251("SO_1251", "낙월도", 35.2, 126.145, 99, 75),
    SO_1252("SO_1252", "외연도항", 36.225, 126.081, 99, 75),
    SO_0757("SO_0757", "안남리", 34.73, 127.264, 99, 75),
    SO_0755("SO_0755", "원동항", 34.393, 126.648, 99, 75),
    SO_0754("SO_0754", "평호리", 34.448, 126.455, 99, 75),
    SO_1256("SO_1256", "어류정항", 37.643, 126.342, 99, 75),
    DT_0064("DT_0064", "교동대교", 37.789, 126.339, 99, 75),
    SO_1249("SO_1249", "독거도", 34.256, 126.178, 99, 75),
    SO_1250("SO_1250", "평도", 34.245, 127.447, 99, 75),
    SO_1253("SO_1253", "상왕등도", 35.658, 126.11, 99, 75),
    SO_1254("SO_1254", "만재도", 34.21, 125.472, 99, 75),
    SO_1248("SO_1248", "신안옥도", 34.683, 126.064, 99, 75),
    SO_0759("SO_0759", "장문리", 34.873, 128.424, 99, 75),
    DT_0068("DT_0068", "위도", 35.618, 126.301, 99, 75),
    SO_0760("SO_0760", "오산항", 36.888, 129.416, 99, 75),
    SO_0753("SO_0753", "하의도웅곡", 34.608, 126.038, 99, 75),
    SO_0631("SO_0631", "암태도", 34.853, 126.071, 99, 75),
    SO_0752("SO_0752", "검산항", 35.0, 126.107, 99, 75),
    SO_0761("SO_0761", "녹동항", 34.527, 127.134, 99, 75),
    DT_0067("DT_0067", "안흥", 36.674, 126.129, 99, 75),
    SO_1255("SO_1255", "상태도", 34.435, 125.285, 99, 75),
    SO_0758("SO_0758", "달천도", 34.761, 127.563, 99, 75),
    SO_0756("SO_0756", "사초항", 34.47, 126.761, 99, 75),
    DT_0063("DT_0063", "가덕도", 35.024, 128.81, 99, 75),
    DT_0062("DT_0062", "마산", 35.197, 128.576, 99, 75),
    SO_1257("SO_1257", "강화하리", 37.728, 126.286, 99, 75),
    DT_0092("DT_0092", "여호항", 34.661, 127.469, 99, 75),
    SO_1258("SO_1258", "잠진도", 37.415, 126.415, 99, 75),
    SO_1259("SO_1259", "자월도", 37.243, 126.318, 99, 75),
    SO_1260("SO_1260", "방포항", 36.502, 126.325, 99, 75),
    SO_1261("SO_1261", "무창포항", 36.249, 126.534, 99, 75),
    SO_1262("SO_1262", "격포항", 35.62, 126.463, 99, 75),
    SO_1263("SO_1263", "구시포항", 35.447, 126.425, 99, 75),
    SO_1264("SO_1264", "계마항", 35.39, 126.401, 99, 75),
    SO_1265("SO_1265", "송이도", 35.271, 126.15, 99, 75),
    SO_1266("SO_1266", "남열항", 34.576, 127.48, 99, 75),
    SO_1267("SO_1267", "구룡포항", 35.99, 129.555, 99, 75),
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