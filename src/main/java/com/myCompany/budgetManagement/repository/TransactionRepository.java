package com.myCompany.budgetManagement.repository;

import com.myCompany.budgetManagement.model.Account;
import com.myCompany.budgetManagement.model.Transaction;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Repository
public class TransactionRepository {

    private Map<Integer, Transaction> repository = new HashMap<>();

    public Transaction save(Transaction transaction) {
        transaction.setId(repository.size()+1);
        return repository.put(repository.size()+1, transaction);
    }

    public Transaction deleteId(Integer id) {
        return Objects.requireNonNull(repository.remove(id));
    }

    public Transaction delete(Transaction transaction) {
        return deleteId(transaction.getId());
    }

    public void deleteAll(){
        repository.clear();
    }

    public List<Transaction> findAll() {
        return repository.values().stream().toList();
    }

    public Transaction findById(Integer id) {
        return repository.get(id);
    }

    public Transaction replace(Integer id, Transaction transaction){
        return repository.replace(id, transaction);
    }

    public List<Transaction> findAllByAccount(Integer AccountId){
        return repository.values().stream()
                .filter(t -> t.getAccount().getId() == AccountId)
                .toList();
    }
}
