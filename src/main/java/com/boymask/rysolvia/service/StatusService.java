package com.boymask.rysolvia.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.boymask.rysolvia.database.status.Status;
import com.boymask.rysolvia.database.status.StatusRepository;

import jakarta.transaction.Transactional;

@Service
public class StatusService {
	private final StatusRepository repo;

	public StatusService(StatusRepository repo) {
		this.repo = repo;
	}

	public List<Status> getAll() {
		return repo.findAll();
	}

	public Status save(Status s) {
		return repo.save(s);
	}

	@Transactional
	public void updateTokens(Long n, boolean inc) {
		Status s;
		List<Status> ll = getAll();
		if (ll.isEmpty()) {
			s = new Status();
			save(s);
		} else
			s = ll.get(0);

		s.setToken_totali(s.getToken_totali() + n);
		if (inc)
			s.setBollette_totali(s.getBollette_totali() + 1);
		save(s);

	}

}
