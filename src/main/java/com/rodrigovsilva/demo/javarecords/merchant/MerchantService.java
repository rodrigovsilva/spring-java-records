package com.rodrigovsilva.demo.javarecords.merchant;


import com.rodrigovsilva.demo.javarecords.dto.Merchant;
import com.rodrigovsilva.demo.javarecords.entity.MerchantEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MerchantService {

    private MerchantRepository merchantRepo;

    public MerchantService(MerchantRepository merchantRepo) {
        this.merchantRepo = merchantRepo;
    }

    @Transactional
    public Merchant createMerchant(Merchant user) {
        var entity = new MerchantEntity(user.username(), user.email());
        var saved = merchantRepo.save(entity);
        return new Merchant(saved.getUsername(), saved.getEmail());
    }


    @Transactional(readOnly = true)
    public List<Merchant> getMerchants() {

        return merchantRepo.loadMerchants();
    }
}
