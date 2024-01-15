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

            return computePie(updatedPieSliceList);
        }

        //add to db;
        PieSliceModel pieSliceModelSave = PieSliceModel.builder().pieSliceId(pieSlice.pie_slice_id())
                .pieName(pieSlice.pie_name()).userId(pieSlice.user_id()).investedMoney(
                pieSlice.invested_money()).ticker(pieSlice.ticker()).shares(pieSlice.shares()).build();
        pieSliceRepository.save(pieSliceModelSave);

        // update User balance
        UserModel currentUser = usersRepository.findByUserId(pieSlice.user_id());
        currentUser.setBalance(currentUser.getBalance() - pieSlice.invested_money());
        usersRepository.save(currentUser);

        pieSliceList.add(pieSliceModelSave);
        return computePie(pieSliceList);
    }

    public Pie getPie(String name) {

        List<PieSliceModel> pieSliceModels = pieSliceRepository.findAllByPieName(name);

        return computePie(pieSliceModels);
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

        return computePie(pieSliceList);
    }

    public List<Pie> getAllUserPies(Long userId) {

        List<PieSliceModel> pieSliceModels = pieSliceRepository.findAllByUserId(userId);
        Map<String, List<PieSliceModel>> piesMap = new HashMap<>();
        Map<String, Double> piesSumGainsMap = new HashMap<>();

        pieSliceModels.forEach(pieSliceModel -> {
            if (!piesMap.containsKey(pieSliceModel.getPieName())) {
                List<PieSliceModel> pieSlices = new ArrayList<>();
                pieSlices.add(pieSliceModel);

                piesMap.put(pieSliceModel.getPieName(), pieSlices);
            } else {
                piesMap.get(pieSliceModel.getPieName()).add(pieSliceModel);
            }
        });

        piesMap.forEach((key, value) -> piesSumGainsMap.put(key, value.stream()
            .mapToDouble(slice -> slice.getShares() * prices.get(slice.getTicker()))
            .sum()));

        List<Pie> pies = new ArrayList<>();
        piesMap.forEach((pieName, slices) -> {
            Pie pie = new Pie();
            List<PieSlice> pieSlices = new ArrayList<>(slices.stream()
                .map(pieSliceModel -> PieSlice.builder()
                    .user_id(pieSliceModel.getUserId())
                    .pie_slice_id(pieSliceModel.getPieSliceId())
                    .pie_name(pieSliceModel.getPieName())
                    .ticker(pieSliceModel.getTicker())
                    .invested_money(pieSliceModel.getInvestedMoney())
                    .shares(pieSliceModel.getShares())
                    .gainsPercentage(
                        pieSliceModel.getShares() * prices.get(pieSliceModel.getTicker())
                            / piesSumGainsMap.get(pieName))
                    .build()).toList());
            pie.getPieSlices().addAll(pieSlices);
            pies.add(pie);
        });

        return pies;
    }

    private Pie computePie(List<PieSliceModel> pieSliceList) {

        double sumGains = pieSliceList.stream()
            .mapToDouble(slice -> slice.getShares() * prices.get(slice.getTicker()))
            .sum();

        Pie pie = new Pie();
        List<PieSlice> pieSlices = new ArrayList<>(pieSliceList.stream()
            .map(pieSliceModel -> PieSlice.builder()
                .user_id(pieSliceModel.getUserId())
                .pie_slice_id(pieSliceModel.getPieSliceId())
                .pie_name(pieSliceModel.getPieName())
                .ticker(pieSliceModel.getTicker())
                .invested_money(pieSliceModel.getInvestedMoney())
                .shares(pieSliceModel.getShares())
                .gainsPercentage(pieSliceModel.getShares() * prices.get(pieSliceModel.getTicker()) / sumGains)
                .build()).toList());
        pie.getPieSlices().addAll(pieSlices);

        return pie;
    }
}
