package com.ndm.ptit.fragment;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.example.do_an_tot_nghiep.Configuration.Constant;
//import com.example.do_an_tot_nghiep.Configuration.HTTPRequest;
//import com.example.do_an_tot_nghiep.Configuration.HTTPService;
//import com.example.do_an_tot_nghiep.Container.WeatherForecast;
//import com.example.do_an_tot_nghiep.Helper.GlobalVariable;
//import com.example.do_an_tot_nghiep.Helper.Tooltip;
//import com.example.do_an_tot_nghiep.Model.Doctor;
//import com.example.do_an_tot_nghiep.Model.Handbook;
//import com.example.do_an_tot_nghiep.Model.Setting;
//import com.example.do_an_tot_nghiep.Model.Speciality;
//import com.example.do_an_tot_nghiep.R;
//import com.example.do_an_tot_nghiep.RecyclerView.ButtonRecyclerView;
//import com.example.do_an_tot_nghiep.RecyclerView.DoctorRecyclerView;
//import com.example.do_an_tot_nghiep.RecyclerView.HandbookRecyclerView;
//import com.example.do_an_tot_nghiep.RecyclerView.SpecialityRecyclerView;
//import com.example.do_an_tot_nghiep.Searchpage.SearchpageActivity;

import com.ndm.ptit.R;
import com.ndm.ptit.activity.LogInActivity;
import com.ndm.ptit.activity.SearchpageActivity;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.dialogs.DialogUtils;
import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.Doctor;
import com.ndm.ptit.enitities.Handbook;
import com.ndm.ptit.enitities.Setting;
import com.ndm.ptit.enitities.Speciality;
import com.ndm.ptit.enitities.services.DoctorService;
import com.ndm.ptit.enitities.speciality.SpecialityResponse;
import com.ndm.ptit.recyclerview.ButtonRecyclerView;
import com.ndm.ptit.recyclerview.DoctorRecyclerView;
import com.ndm.ptit.recyclerview.DoctorRecyclerViewSpeciality;
import com.ndm.ptit.recyclerview.HandbookRecyclerView;
import com.ndm.ptit.recyclerview.SpecialityRecyclerView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * @author Phong-Kaster
 * @since 17-11-2022
 * Home fragment
 */
public class MainFragment extends Fragment {
    private final String TAG = "Home Fragment";

    private RecyclerView recyclerViewSpeciality;
    private RecyclerView recyclerViewDoctor;
    private RecyclerView recyclerViewHandbook;
    private RecyclerView recyclerViewRecommendedPages;


    private EditText searchBar;
    private TextView txtReadMoreSpeciality;
    private TextView txtReadMoreDoctor;

    private Context context;
    private RecyclerView recyclerViewButton;

    private TextView txtDate;
    private TextView txtWeather;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        setupComponent(view);
//        setupViewModel();

        setupEvent();
//        getCurrentWeather();

        setupRecyclerViewButton();
        setupRecyclerViewHandbook();
        setupRecyclerViewRecommendedPages();

        fetchSpecialities();
        fetchDoctors();

        return view;
    }


    private void setupComponent(View view) {
        context = requireContext();
        //    globalVariable = (GlobalVariable) requireActivity().getApplication();

        recyclerViewSpeciality = view.findViewById(R.id.recyclerViewSpeciality);
        recyclerViewDoctor = view.findViewById(R.id.recyclerViewDoctor);
        recyclerViewButton = view.findViewById(R.id.recyclerViewButton);
        recyclerViewHandbook = view.findViewById(R.id.recyclerViewHandbook);
        recyclerViewRecommendedPages = view.findViewById(R.id.recyclerViewRecommendedPages);

        searchBar = view.findViewById(R.id.searchBar);
        txtReadMoreSpeciality = view.findViewById(R.id.txtReadMoreSpeciality);
        txtReadMoreDoctor = view.findViewById(R.id.txtReadMoreDoctor);

//        txtWeather = view.findViewById(R.id.txtWeather);
        txtDate = view.findViewById(R.id.txtDate);
    }


    private void setupRecyclerViewSpeciality(List<SpecialityResponse> list) {
        SpecialityRecyclerView specialityAdapter = new SpecialityRecyclerView(requireActivity(), list, R.layout.recycler_view_element_speciality);
        recyclerViewSpeciality.setAdapter(specialityAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewSpeciality.setLayoutManager(manager);
    }


    private void setupRecyclerViewDoctor(List<DoctorService> list) {
        DoctorRecyclerViewSpeciality doctorAdapter = new DoctorRecyclerViewSpeciality(requireActivity(), list);
        recyclerViewDoctor.setAdapter(doctorAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewDoctor.setLayoutManager(manager);
    }


    /**
     * @since 18-11-2022
     * setup event for buttons
     */
    @SuppressLint({"UnspecifiedImmutableFlag", "ShortAlarm"})
    private void setupEvent() {
        /*SEARCH BAR*/
        searchBar.setOnClickListener(view -> {
            Intent intent = new Intent(requireContext(), SearchpageActivity.class);
            startActivity(intent);
        });


        /*TXT READ MORE SPECIALITY*/
        txtReadMoreSpeciality.setOnClickListener(view -> {
            Intent intent = new Intent(context, SearchpageActivity.class);
            String filterKey = context.getString(R.string.speciality);

            intent.putExtra("filterKey", filterKey);
            startActivity(intent);
        });

        /*TXT READ MORE DOCTOR*/
        txtReadMoreDoctor.setOnClickListener(view -> {
            Intent intent = new Intent(context, SearchpageActivity.class);
            String filterKey = context.getString(R.string.doctor);

            intent.putExtra("filterKey", filterKey);
            startActivity(intent);
        });
    }


    private void setupRecyclerViewButton() {
        Setting setting0 = new Setting(R.drawable.ic_i_exam_speciality, "niềng răng", "niềng răng");
        Setting setting1 = new Setting(R.drawable.ic_exam_general, "khám xoang", "khám xoang");
        Setting setting2 = new Setting(R.drawable.ic_exam_heart, "khám điện não đồ", "khám điện não đồ");
        Setting setting3 = new Setting(R.drawable.ic_exam_pregnant, "khám sản khoa", "khám sản khoa");
        Setting setting4 = new Setting(R.drawable.ic_exam_tooth, "khám răng", "khám răng");
        Setting setting5 = new Setting(R.drawable.ic_exam_eye, "khám mắt", "khám mắt");
        Setting setting6 = new Setting(R.drawable.ic_exam_medical_test, "chấn thương dây chằng", "chấn thương dây chằng");
        Setting setting7 = new Setting(R.drawable.ic_exam_covid19, "đau xương khớp", "đau xương khớp");

        List<Setting> list = new ArrayList<>();
        list.add(setting0);
        list.add(setting1);
        list.add(setting2);
        list.add(setting3);
        list.add(setting4);
        list.add(setting5);
        list.add(setting6);
        list.add(setting7);

        ButtonRecyclerView buttonAdapter = new ButtonRecyclerView(requireActivity(), list);
        recyclerViewButton.setAdapter(buttonAdapter);

        GridLayoutManager manager = new GridLayoutManager(requireContext(), 3);
        recyclerViewButton.setLayoutManager(manager);
    }

    private void setupRecyclerViewHandbook() {
        Handbook handbook0 = new Handbook(
                "https://static-images.vnncdn.net/files/publish/2022/12/20/benh-vien-viet-duc-290.jpg",
                "Ba tiếng chờ đợi, 10 phút khám - Xếp hàng dài, căng tai nghe để không mất lượt",
                "https://vietnamnet.vn/ba-tieng-cho-doi-10-phut-kham-2092139.html");

        Handbook handbook1 = new Handbook(
                "https://i.ytimg.com/vi/muEAnbsjzQ4/maxresdefault.jpg",
                "Umbrella Corporation - luôn bảo vệ sức khỏe bạn",
                "https://www.youtube.com/watch?v=W4JA4dbscis&ab_channel=UmbrellaCorporation4");

        Handbook handbook2 = new Handbook(
                "https://image.thanhnien.vn/w2048/Uploaded/2022/uqvpoqiw/2022_12_18/giay-ve-sinh-9535.jpg",
                "Chuyên gia nói gì về giờ đi vệ sinh tốt nhất trong ngày?",
                "https://thanhnien.vn/chuyen-gia-noi-gi-ve-gio-di-ve-sinh-tot-nhat-trong-ngay-post1533096.html");
        Handbook handbook3 = new Handbook(
                "https://image.thanhnien.vn/w2048/Uploaded/2022/wobjohb/2022_12_17/p1-9988.jpeg",
                "Đâu là dấu hiệu cảnh báo một người sắp qua đời?",
                "https://thanhnien.vn/dau-la-dau-hieu-canh-bao-mot-nguoi-sap-qua-doi-post1533048.html");
        Handbook handbook4 = new Handbook(
                "https://image.thanhnien.vn/w2048/Uploaded/2022/uqvpoqiw/2022_12_18/intermittent-fasting3-8817.jpg",
                "Đã tìm ra cách có thể 'chữa khỏi' bệnh tiểu đường?",
                "https://thanhnien.vn/da-tim-ra-cach-co-the-chua-khoi-benh-tieu-duong-post1532438.html");
        Handbook handbook5 = new Handbook(
                "https://image.thanhnien.vn/w2048/Uploaded/2022/aeymrexqam/2022_12_19/ph-1-3698.jpg",
                "Giải pháp cải thiện ho, khò khè, khó thở",
                "https://thanhnien.vn/giai-phap-cai-thien-ho-kho-khe-kho-tho-post1533364.html");
        Handbook handbook6 = new Handbook(
                "https://image.thanhnien.vn/w2048/Uploaded/2022/otpfeclyrbn/2022_12_19/1-4028.jpg",
                "Vì sao thủy đậu, sởi nguy hiểm dịp tết?",
                "https://thanhnien.vn/vi-sao-thuy-dau-soi-nguy-hiem-dip-tet-post1533380.html");
        Handbook handbook7 = new Handbook(
                "https://image.thanhnien.vn/w2048/Uploaded/2022/pwvosgyk/2022_12_16/image1-725.jpg",
                "Sữa tắm chiết xuất lá trầu không với công thức kháng khuẩn không gây khô da",
                "https://thanhnien.vn/sua-tam-chiet-xuat-la-trau-khong-voi-cong-thuc-khang-khuan-khong-gay-kho-da-post1532652.html");
        Handbook handbook8 = new Handbook(
                "https://image.thanhnien.vn/w2048/Uploaded/2022/uqvpoqiw/2022_12_10/whole-grain-bread-1-4791.jpg",
                "5 loại thực phẩm phổ biến có thể gây mệt mỏi kinh niên",
                "https://thanhnien.vn/5-loai-thuc-pham-pho-bien-co-the-gay-met-moi-kinh-nien-post1530554.html");
        Handbook handbook9 = new Handbook(
                "https://image.thanhnien.vn/w2048/Uploaded/2022/wobjohb/2022_12_19/skmoi-4370.png",
                "Làm điều này sau mỗi bữa ăn, bạn sẽ sống thọ hơn",
                "https://thanhnien.vn/lam-dieu-nay-sau-moi-bua-an-ban-se-song-tho-hon-post1532440.html");
        Handbook handbook10 = new Handbook(
                "https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2022/12/18/photo-1671368369791-16713683707381806268917.jpg",
                "Chế độ ăn uống lành mạnh có lợi cho mạch máu",
                "https://suckhoedoisong.vn/che-do-an-uong-lanh-manh-co-loi-cho-mach-mau-169221218202106904.htm");
        Handbook handbook11 = new Handbook(
                "https://suckhoedoisong.qltns.mediacdn.vn/thumb_w/640/324455921873985536/2022/12/18/benh-phoi-tac-nghen-man-tinh-nen-an-gi1-167137876802272682938.jpg",
                "Người bệnh phổi tắc nghẽn mạn tính cần được chăm sóc và ăn uống thế nào trong mùa đông?",
                "https://suckhoedoisong.vn/nguoi-benh-phoi-tac-nghen-man-tinh-can-duoc-cham-soc-va-an-uong-the-nao-trong-mua-dong-169221218231139085.htm"
        );
        Handbook handbook12 = new Handbook(
                "https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2022/12/16/photo-1671178122606-16711781237731874719199.jpg",
                "8 thực phẩm bổ dưỡng giúp bạn ấm áp trong thời tiết lạnh",
                "https://suckhoedoisong.vn/8-thuc-pham-bo-duong-giup-ban-am-ap-trong-thoi-tiet-lanh-169221216152025663.htm");
        Handbook handbook13 = new Handbook(
                "https://suckhoedoisong.qltns.mediacdn.vn/thumb_w/640/324455921873985536/2022/12/16/ca-rota-1671208804452168719323.jpg",
                "7 loại rau củ tốt nhất cho người bệnh gan nhiễm mỡ",
                "https://suckhoedoisong.vn/7-loai-rau-cu-tot-nhat-cho-nguoi-benh-gan-nhiem-mo-169221216234817123.htm");
        Handbook handbook14 = new Handbook(
                "https://suckhoedoisong.qltns.mediacdn.vn/thumb_w/640/324455921873985536/2022/12/16/a-va-nguoi-benh-dai-thao-duong-an-gao-lut-the-nao-16711647068152070374816.jpg",
                "Bí quyết ăn cơm gạo lứt tốt nhất cho người bệnh đái tháo đường",
                "https://suckhoedoisong.vn/bi-quyet-an-com-gao-lut-tot-nhat-cho-nguoi-benh-dai-thao-duong-169221216114128467.htm");

        Handbook handbook15 = new Handbook(
                "https://suckhoedoisong.qltns.mediacdn.vn/thumb_w/640/324455921873985536/2022/12/1/dtd2-16698836686521411857134.jpg",
                "10 loại thực phẩm người bệnh đái tháo đường nên tránh",
                "https://suckhoedoisong.vn/10-loai-thuc-pham-nguoi-benh-dai-thao-duong-nen-tranh-169221201153930742.htm");

        Handbook handbook16 = new Handbook(
                "https://suckhoedoisong.qltns.mediacdn.vn/324455921873985536/2022/12/12/rau-16708612126451164928123.jpg",
                "Lời khuyên về chế độ ăn uống và lối sống dành cho người bệnh gan nhiễm mỡ",
                "https://suckhoedoisong.vn/loi-khuyen-ve-che-do-an-uong-va-loi-song-danh-cho-nguoi-benh-gan-nhiem-mo-169221212231350623.htm"
        );
        Handbook handbook17 = new Handbook(
                "https://cdn.bookingcare.vn/fr/w1000/2022/12/18/164937-dia-chi-kham-tai-mui-hong-quan-binh-thanh.png",
                "Top 5 phòng khám Tai mũi họng tốt quận Bình Thạnh",
                "https://bookingcare.vn/cam-nang/top-5-phong-kham-tai-mui-hong-tot-quan-binh-thanh-p3048.html");

        List<Handbook> list = new ArrayList<>();
        list.add(handbook0);
        list.add(handbook1);
        list.add(handbook2);
        list.add(handbook3);
        list.add(handbook4);
        list.add(handbook5);
        list.add(handbook6);
        list.add(handbook7);
        list.add(handbook8);
        list.add(handbook9);
        list.add(handbook10);
        list.add(handbook11);
        list.add(handbook12);
        list.add(handbook13);
        list.add(handbook14);
        list.add(handbook15);
        list.add(handbook16);
        list.add(handbook17);

        HandbookRecyclerView handbookAdapter = new HandbookRecyclerView(requireActivity(), list, R.layout.recycler_view_element_handbook);
        recyclerViewHandbook.setAdapter(handbookAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewHandbook.setLayoutManager(manager);
    }

    /**
     * @since 20-12-2022
     * setup recycler view recommended pages
     */
    private void setupRecyclerViewRecommendedPages() {
        Handbook handbook0 = new Handbook(
                "https://suckhoedoisong.qltns.mediacdn.vn/thumb_w/1200/324455921873985536/2021/10/10/baoskds-1633861575912643818497-49-0-295-393-crop-1633861583124643700229.png",
                "Báo sức khỏe đời sống - cơ quan ngôn luận của Bộ Y tế",
                "https://suckhoedoisong.vn/dinh-duong.htm");

        Handbook handbook1 = new Handbook(
                "https://play-lh.googleusercontent.com/WKLidAunta9pcv-nvtXaln9LY6YkGUdYN3GOfivc4ti4mfGEHEq1MOM0DN8U2Ic6oJw=w480-h960-rw",
                "Sức Khoẻ - Sổ tay dinh dưỡng, giữ gìn đời sống gia đình",
                "https://thanhnien.vn/suc-khoe/");

        Handbook handbook2 = new Handbook(
                "https://static.mediacdn.vn/covid19.gov.vn/image/default_share.jpg",
                "Bộ Y tế - Cổng thông tin điện tử",
                "https://moh.gov.vn/");

        Handbook handbook3 = new Handbook(
                "https://upload.wikimedia.org/wikipedia/vi/thumb/2/22/Vietnamnet-Logo.png/285px-Vietnamnet-Logo.png",
                "Sức khỏe đời sống - Cẩm nang chăm sóc sức khỏe gia đình",
                "https://vietnamnet.vn/suc-khoe"
        );


        List<Handbook> list = new ArrayList<>();
        list.add(handbook0);
        list.add(handbook1);
        list.add(handbook2);
        list.add(handbook3);

        HandbookRecyclerView handbookAdapter = new HandbookRecyclerView(requireActivity(), list, R.layout.recycler_view_element_handbook_2);
        recyclerViewRecommendedPages.setAdapter(handbookAdapter);

        LinearLayoutManager manager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewRecommendedPages.setLayoutManager(manager);
    }

    private void fetchSpecialities() {
        // Lấy token từ SharedPreferences
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse<SpecialityResponse>> call = apiService.getAllSpecialities("Bearer " + token);

        call.enqueue(new Callback<BaseResponse<SpecialityResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<SpecialityResponse>> call, Response<BaseResponse<SpecialityResponse>> response) {
                if (response.isSuccessful()) {
                    BaseResponse<SpecialityResponse> specialityResponse = response.body();
                    if (specialityResponse != null && specialityResponse.getResult() == 1) {
                        // Lấy danh sách chuyên khoa
                        List<SpecialityResponse> specialities = specialityResponse.getData();
                        setupRecyclerViewSpeciality(specialities);

                    } else {
                        DialogUtils.showErrorDialog(getContext(), specialityResponse.getMsg());
                    }
                } else {
                    DialogUtils.showErrorDialog(getContext(), "Request failed");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<SpecialityResponse>> call, Throwable t) {
                DialogUtils.showErrorDialog(getContext(), t.getMessage());
            }
        });
    }

    private void fetchDoctors() {
        SharedPreferences prefs = getActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        ApiService apiService = RetrofitClient.getClient().create(ApiService.class);
        Call<BaseResponse<DoctorService>> call = apiService.doctorReadAll("Bearer " + token);
        call.enqueue(new Callback<BaseResponse<DoctorService>>() {
            @Override
            public void onResponse(Call<BaseResponse<DoctorService>> call, Response<BaseResponse<DoctorService>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<DoctorService> allDoctors = response.body().getData();
                    setupRecyclerViewDoctor(allDoctors);
                } else {
                    DialogUtils.showErrorDialog(getContext(), "Request failed");
                }
            }

            @Override
            public void onFailure(Call<BaseResponse<DoctorService>> call, Throwable t) {
                Log.d("vao day safasf", "asfasf");
            }
        });
    }


//    private void getCurrentWeather()
//    {
//        Retrofit service = HTTPService.getOpenWeatherMapInstance();
//        HTTPRequest api = service.create(HTTPRequest.class);
//
//        /*Step 3*/
//        Map<String, String> parameters = new HashMap<>();
//        String latitude = "10.8333";// kinh độ TP.HCM
//        String longitude = "106.6667";// vĩ độ TP.HCM
//        String apiKey = Constant.OPEN_WEATHER_MAP_API_KEY();// api key của mình trên open weather map.org
//
//        parameters.put("lat", latitude);
//        parameters.put("lon", longitude);
//        parameters.put("appid", apiKey);
//        Call<WeatherForecast> container = api.getCurrentWeather(parameters);
//
//        /*Step 4*/
//        container.enqueue(new Callback<WeatherForecast>() {
//            @Override
//            public void onResponse(@NonNull Call<WeatherForecast> call, @NonNull Response<WeatherForecast> response) {
//                if(response.isSuccessful())
//                {
//                    WeatherForecast content = response.body();
//                    assert content != null;
////                    System.out.println(TAG);
////                    System.out.println("timezone: " + content.getTimeZone());
////                    System.out.println("name: " + content.getName());
//                    printDateAndWeather(content);
//
//                }
//                if(response.errorBody() != null)
//                {
//                    try
//                    {
//                        JSONObject jObjError = new JSONObject(response.errorBody().string());
//                        System.out.println( jObjError );
//                    }
//                    catch (Exception e) {
//                        System.out.println( e.getMessage() );
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<WeatherForecast> call, @NonNull Throwable t) {
//                System.out.println(TAG);
//                System.out.println("Error: " + t.getMessage());
//            }
//        });
//    }
//
//    /**
//     * @since 22-12-2022
//     * in ngày hôm nay và thời tiết lên màn hình
//     */
//    private void printDateAndWeather(WeatherForecast content)
//    {
//        String today = Tooltip.getReadableToday(context);
//        txtDate.setText(today);
//
//        String temperature = String.valueOf(content.getMain().getTemp());
//        String weatherInfo = temperature.substring(0,2) + getString(R.string.celsius);
//        txtWeather.setText(weatherInfo);
//    }
}