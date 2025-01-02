package com.ndm.ptit.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ndm.ptit.R;
import com.ndm.ptit.adapter.FilterOptionAdapter;
import com.ndm.ptit.api.ApiService;
import com.ndm.ptit.api.RetrofitClient;
import com.ndm.ptit.enitities.BaseResponse;
import com.ndm.ptit.enitities.Option;
import com.ndm.ptit.enitities.services.DoctorService;
import com.ndm.ptit.enitities.services.Services;
import com.ndm.ptit.enitities.speciality.SpecialityResponse;
import com.ndm.ptit.helper.GlobalVariable;
import com.ndm.ptit.recyclerview.DoctorRecyclerView;
import com.ndm.ptit.recyclerview.ServiceRecyclerView;
import com.ndm.ptit.recyclerview.SpecialityRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchpageActivity extends AppCompatActivity {
    private static final String TAG = "SearchpageActivity";
    private static final long SEARCH_DELAY_MS = 300;

    private ImageButton btnBack;
    private Spinner sprFilter;
    private SearchView searchView;
    private ProgressBar progressBar;
    private RecyclerView doctorRecyclerView;
    private RecyclerView specialityRecyclerView;
    private RecyclerView serviceRecyclerView;

    private GlobalVariable globalVariable;
    private ApiService apiService;
    private String filterKey;
    private final Handler searchHandler = new Handler(Looper.getMainLooper());
    private Runnable searchRunnable;

    // Store original data
    private List<DoctorService> allDoctors = new ArrayList<>();
    private List<SpecialityResponse> allSpecialities = new ArrayList<>();
    private List<Services> allServices = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchpage);

        initializeViews();
        setupApiService();
        setupFilterSpinner();
        handleInitialFilter();
        setupSearchView();
        setupBackButton();
        setupRecyclerViews();

        // Initial data load
        sendRequestWithFilterKey();
    }

    private void initializeViews() {
        btnBack = findViewById(R.id.btnBack);
        sprFilter = findViewById(R.id.sprFilter);
        searchView = findViewById(R.id.searchView);
        progressBar = findViewById(R.id.progressBar);
        doctorRecyclerView = findViewById(R.id.doctorRecyclerView);
        specialityRecyclerView = findViewById(R.id.specialityRecyclerView);
        serviceRecyclerView = findViewById(R.id.serviceRecyclerView);

        globalVariable = (GlobalVariable) getApplication();
        filterKey = getString(R.string.service);
    }

    private void setupRecyclerViews() {
        doctorRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        specialityRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        serviceRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupApiService() {
        apiService = RetrofitClient.getClient().create(ApiService.class);
    }

    private void handleInitialFilter() {
        String intentFilterKey = getIntent().getStringExtra("filterKey");
        if (intentFilterKey != null) {
            filterKey = intentFilterKey;
            int position = getFilterPosition(filterKey);
            sprFilter.setSelection(position);
        }
    }

    private int getFilterPosition(String filterKey) {
        if (filterKey.equals(getString(R.string.speciality))) {
            return 1;
        } else if (filterKey.equals(getString(R.string.doctor))) {
            return 2;
        }
        return 0; // Default to service
    }

    private void setupFilterSpinner() {
        List<Option> filterOptions = globalVariable.getFilterOptions();
        FilterOptionAdapter filterAdapter = new FilterOptionAdapter(this, filterOptions);
        sprFilter.setAdapter(filterAdapter);
        sprFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String newFilterKey = filterOptions.get(position).getName();
                if (!newFilterKey.equals(filterKey)) {
                    filterKey = newFilterKey;
                    searchView.setQuery("", false);
                    sendRequestWithFilterKey();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void setupSearchView() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                performSearch(query);
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchHandler.removeCallbacks(searchRunnable);
                searchRunnable = () -> performSearch(newText);
                searchHandler.postDelayed(searchRunnable, SEARCH_DELAY_MS);
                return true;
            }
        });
    }

    private void setupBackButton() {
        btnBack.setOnClickListener(v -> finish());
    }

    private void sendRequestWithFilterKey() {
        SharedPreferences prefs = getSharedPreferences("user_prefs", Context.MODE_PRIVATE);
        String token = prefs.getString("token", null);

        if (token == null) {
            Toast.makeText(this, "Unauthorized access", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        setLoadingState(true);
        hideAllRecyclerViews();

        String authToken = "Bearer " + token;
        if (filterKey.equals(getString(R.string.doctor))) {
            fetchDoctors(authToken);
        } else if (filterKey.equals(getString(R.string.speciality))) {
            fetchSpecialities(authToken);
        } else if (filterKey.equals(getString(R.string.service))) {
            fetchServices(authToken);
        }
    }

    private void fetchDoctors(String token) {
        Call<BaseResponse<DoctorService>> call = apiService.doctorReadAll(token);
        call.enqueue(new Callback<BaseResponse<DoctorService>>() {
            @Override
            public void onResponse(Call<BaseResponse<DoctorService>> call, Response<BaseResponse<DoctorService>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allDoctors = response.body().getData();
                    performSearch(searchView.getQuery().toString());
                } else {
                    handleApiError(new Exception("Failed to fetch doctors"));
                }
                setLoadingState(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<DoctorService>> call, Throwable t) {
                handleApiError(t);
            }
        });
    }

    private void fetchSpecialities(String token) {
        Call<BaseResponse<SpecialityResponse>> call = apiService.specialityReadAll(token);
        call.enqueue(new Callback<BaseResponse<SpecialityResponse>>() {
            @Override
            public void onResponse(Call<BaseResponse<SpecialityResponse>> call, Response<BaseResponse<SpecialityResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allSpecialities = response.body().getData();
                    performSearch(searchView.getQuery().toString());
                } else {
                    handleApiError(new Exception("Failed to fetch specialities"));
                }
                setLoadingState(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<SpecialityResponse>> call, Throwable t) {
                handleApiError(t);
            }
        });
    }

    private void fetchServices(String token) {
        Call<BaseResponse<Services>> call = apiService.serviceReadAll(token);
        call.enqueue(new Callback<BaseResponse<Services>>() {
            @Override
            public void onResponse(Call<BaseResponse<Services>> call, Response<BaseResponse<Services>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allServices = response.body().getData();
                    performSearch(searchView.getQuery().toString());
                } else {
                    handleApiError(new Exception("Failed to fetch services"));
                }
                setLoadingState(false);
            }

            @Override
            public void onFailure(Call<BaseResponse<Services>> call, Throwable t) {
                handleApiError(t);
            }
        });
    }

    private void performSearch(String query) {
        hideAllRecyclerViews();

        if (filterKey.equals(getString(R.string.doctor))) {
            List<DoctorService> filteredDoctors = filterDoctors(allDoctors, query);
            setupRecyclerViewDoctor(filteredDoctors);
        } else if (filterKey.equals(getString(R.string.speciality))) {
            List<SpecialityResponse> filteredSpecialities = filterSpecialities(allSpecialities, query);
            setupRecyclerViewSpeciality(filteredSpecialities);
        } else if (filterKey.equals(getString(R.string.service))) {
            List<Services> filteredServices = filterServices(allServices, query);
            setupRecyclerViewService(filteredServices);
        }
    }

    private List<DoctorService> filterDoctors(List<DoctorService> doctors, String query) {
        if (TextUtils.isEmpty(query)) return doctors;

        String lowerQuery = query.toLowerCase().trim();
        return doctors.stream()
                .filter(doctor ->
                        (doctor.getName() != null && doctor.getName().toLowerCase().contains(lowerQuery)) ||
                                (doctor.getDescription() != null && doctor.getDescription().toLowerCase().contains(lowerQuery)))
                .collect(Collectors.toList());
    }

    private List<SpecialityResponse> filterSpecialities(List<SpecialityResponse> specialities, String query) {
        if (TextUtils.isEmpty(query)) return specialities;

        String lowerQuery = query.toLowerCase().trim();
        return specialities.stream()
                .filter(speciality ->
                        (speciality.getName() != null && speciality.getName().toLowerCase().contains(lowerQuery)) ||
                                (speciality.getDescription() != null && speciality.getDescription().toLowerCase().contains(lowerQuery)))
                .collect(Collectors.toList());
    }

    private List<Services> filterServices(List<Services> services, String query) {
        if (TextUtils.isEmpty(query)) return services;

        String lowerQuery = query.toLowerCase().trim();
        return services.stream()
                .filter(service ->
                        (service.getName() != null && service.getName().toLowerCase().contains(lowerQuery)) ||
                                (service.getDescription() != null && service.getDescription().toLowerCase().contains(lowerQuery)))
                .collect(Collectors.toList());
    }

    private void setupRecyclerViewDoctor(List<DoctorService> doctors) {
        hideAllRecyclerViews();
        DoctorRecyclerView doctorAdapter = new DoctorRecyclerView(this, doctors);
        doctorRecyclerView.setAdapter(doctorAdapter);
        doctorRecyclerView.setVisibility(View.VISIBLE);

        if (doctors.isEmpty()) {
            showNoResultsMessage();
        }
    }

    private void setupRecyclerViewSpeciality(List<SpecialityResponse> specialities) {
        hideAllRecyclerViews();
        SpecialityRecyclerView specialityAdapter = new SpecialityRecyclerView(this, specialities, R.layout.recycler_view_element_speciality_2);
        specialityRecyclerView.setAdapter(specialityAdapter);
        specialityRecyclerView.setVisibility(View.VISIBLE);

        if (specialities.isEmpty()) {
            showNoResultsMessage();
        }
    }

    private void setupRecyclerViewService(List<Services> services) {
        hideAllRecyclerViews();
        ServiceRecyclerView serviceAdapter = new ServiceRecyclerView(this, services);
        serviceRecyclerView.setAdapter(serviceAdapter);
        serviceRecyclerView.setVisibility(View.VISIBLE);

        if (services.isEmpty()) {
            showNoResultsMessage();
        }
    }

    private void hideAllRecyclerViews() {
        doctorRecyclerView.setVisibility(View.GONE);
        specialityRecyclerView.setVisibility(View.GONE);
        serviceRecyclerView.setVisibility(View.GONE);
    }

    private void setLoadingState(boolean isLoading) {
        if (progressBar != null) {
            progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        }
    }

    private void showNoResultsMessage() {
        Toast.makeText(this, "No results found", Toast.LENGTH_SHORT).show();
    }

    private void handleApiError(Throwable t) {
        setLoadingState(false);
        t.printStackTrace();
        Toast.makeText(this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (searchHandler != null && searchRunnable != null) {
            searchHandler.removeCallbacks(searchRunnable);
        }
    }
}