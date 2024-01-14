package com.example.demo;

import com.example.demo.model.PieSliceModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.PieSliceRepository;
import com.example.demo.repository.UsersRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PieService {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PieSliceRepository pieSliceRepository;

    @Autowired
    private UsersRepository usersRepository;

    public String addSlice(String body) {

        PieSlice pieSlice = null;
        try {
            pieSlice = mapper.readValue(body, PieSlice.class);
        } catch (IOException e) {
            return "error";
        }

        //add to db;
        PieSliceModel pieSliceModel = PieSliceModel.builder().pieSliceId(pieSlice.pie_slice_id())
                .pieName(pieSlice.pie_name()).userId(pieSlice.user_id()).investedMoney(
                pieSlice.invested_money()).ticker(pieSlice.ticker()).shares(pieSlice.shares()).build();
        pieSliceRepository.save(pieSliceModel);

        UserModel currentUser = usersRepository.findByUserId(pieSlice.user_id());

        currentUser.setBalance(currentUser.getBalance() - pieSlice.invested_money());
        usersRepository.save(currentUser);

        // compute pie
        List<PieSliceModel> pieSliceList = pieSliceRepository.findAllByUserIdAndPieName(
            pieSlice.user_id(), pieSlice.pie_name());

        Pie pie = new Pie();
        pie.getPieSlices().addAll(pieSliceList);

        return pie.toString();
    }

    public Pie getPie(String name) {

        List<PieSliceModel> pieSliceModels = pieSliceRepository.findAllByPieName(name);

        Pie pie = new Pie();
        pie.getPieSlices().addAll(pieSliceModels);

        System.out.println(pie.toString());

        return pie;
    }
}
