package com.example.demo;

import static com.example.demo.BsdBeApplication.prices;

import com.example.demo.model.PieSliceModel;
import com.example.demo.model.UserModel;
import com.example.demo.repository.PieSliceRepository;
import com.example.demo.repository.UsersRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PieService {

    ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private PieSliceRepository pieSliceRepository;
    @Autowired
    private UsersRepository usersRepository;

    public Pie addSlice(String body) {

        final PieSlice pieSlice;
        try {
            pieSlice = mapper.readValue(body, PieSlice.class);
        } catch (IOException e) {
            return new Pie();
        }

        List<PieSliceModel> pieSliceList = pieSliceRepository.findAllByUserIdAndPieName(
            pieSlice.user_id(), pieSlice.pie_name());
        List<PieSliceModel> filterByTicker = pieSliceList.stream()
            .filter(slice -> slice.getTicker().equals(pieSlice.ticker())).toList();
        if (!filterByTicker.isEmpty()) {
            PieSliceModel sliceToUpdate = filterByTicker.get(0);
            sliceToUpdate.setInvestedMoney(sliceToUpdate.getInvestedMoney() + pieSlice.invested_money());
            sliceToUpdate.setShares(sliceToUpdate.getShares() + pieSlice.shares());

            pieSliceRepository.save(sliceToUpdate);

            UserModel currentUser = usersRepository.findByUserId(pieSlice.user_id());
            currentUser.setBalance(currentUser.getBalance() - pieSlice.invested_money());
            usersRepository.save(currentUser);

            List<PieSliceModel> updatedPieSliceList = pieSliceRepository.findAllByUserIdAndPieName(
                pieSlice.user_id(), pieSlice.pie_name());
            Pie pie = new Pie();
            pie.getPieSlices().addAll(updatedPieSliceList);

            return pie;
        }

        //add to db;
        PieSliceModel pieSliceModel = PieSliceModel.builder().pieSliceId(pieSlice.pie_slice_id())
                .pieName(pieSlice.pie_name()).userId(pieSlice.user_id()).investedMoney(
                pieSlice.invested_money()).ticker(pieSlice.ticker()).shares(pieSlice.shares()).build();
        pieSliceRepository.save(pieSliceModel);

        // update User balance
        UserModel currentUser = usersRepository.findByUserId(pieSlice.user_id());
        currentUser.setBalance(currentUser.getBalance() - pieSlice.invested_money());
        usersRepository.save(currentUser);

        // compute pie
        Pie pie = new Pie();
        pieSliceList.add(pieSliceModel);
        pie.getPieSlices().addAll(pieSliceList);

        return pie;
    }

    public Pie getPie(String name) {

        List<PieSliceModel> pieSliceModels = pieSliceRepository.findAllByPieName(name);

        Pie pie = new Pie();
        pie.getPieSlices().addAll(pieSliceModels);

        System.out.println(pie.toString());

        return pie;
    }

    public Pie sell(String body) throws JsonProcessingException {

        List<PieSliceSell> pieSliceSellList = new ArrayList<>();
        try {
            pieSliceSellList = new ObjectMapper().readValue(
                body,
                new TypeReference<List<PieSliceSell>>() {
                });
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        }

        pieSliceSellList
            .forEach(pieSliceSell -> {
                PieSliceModel pieSlice = pieSliceRepository.findPieSliceModelByPieSliceId(pieSliceSell.pie_slice_id());

                // Sell whole slice
                if (pieSliceSell.shares().equals(pieSlice.getShares())) {
                    pieSliceRepository.delete(pieSlice);
                } else if (pieSliceSell.shares() <= pieSlice.getShares()) {
                    pieSlice.setShares(pieSlice.getShares() - pieSliceSell.shares());
                    pieSliceRepository.save(pieSlice);
                } else {
                    throw new RuntimeException(String.format("You want to sell %,.4f shares but you have only %,.4f", pieSliceSell.shares(), pieSlice.getShares()));
                }

                // Update user balance
                UserModel user = usersRepository.findByUserId(pieSliceSell.user_id());
                Double gainedMoney = pieSliceSell.shares() * prices.get(pieSliceSell.ticker());
                System.out.println("Gained money: " + gainedMoney);
                user.setBalance(user.getBalance() + gainedMoney);
                usersRepository.save(user);
            });

        // compute pie
        List<PieSliceModel> pieSliceList = pieSliceRepository.findAllByUserIdAndPieName(
            pieSliceSellList.get(0).user_id(), pieSliceSellList.get(0).pie_name());

        Pie pie = new Pie();
        pie.getPieSlices().addAll(pieSliceList);

        return pie;
    }

    public List<Pie> getAllUserPies(Long userId) {

        List<PieSliceModel> pieSliceModels = pieSliceRepository.findAllByUserId(userId);
        Map<String, List<PieSliceModel>> piesMap = new HashMap<>();

        pieSliceModels.forEach(pieSliceModel -> {
            if (!piesMap.containsKey(pieSliceModel.getPieName())) {
                List<PieSliceModel> pieSlices = new ArrayList<>();
                pieSlices.add(pieSliceModel);

                piesMap.put(pieSliceModel.getPieName(), pieSlices);
            } else {
                piesMap.get(pieSliceModel.getPieName()).add(pieSliceModel);
            }
        });

        List<Pie> pies = new ArrayList<>();
        piesMap.values().forEach(slices -> {
            Pie pie = new Pie();
            pie.getPieSlices().addAll(slices);
            pies.add(pie);
        });

        return pies;
    }
}
