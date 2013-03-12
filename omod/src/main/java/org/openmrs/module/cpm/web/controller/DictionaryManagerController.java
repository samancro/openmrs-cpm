package org.openmrs.module.cpm.web.controller;

import org.openmrs.api.context.Context;
import org.openmrs.module.cpm.ProposedConceptResponse;
import org.openmrs.module.cpm.ProposedConceptResponsePackage;
import org.openmrs.module.cpm.api.ProposedConceptService;
import org.openmrs.module.cpm.web.dto.ConceptDto;
import org.openmrs.module.cpm.web.dto.SubmissionDto;
import org.openmrs.module.cpm.web.dto.SubmissionResponseDto;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Controller
public class DictionaryManagerController {

	//
	// Pages
	//

    @RequestMapping(value = "module/cpm/proposalReview.list", method = RequestMethod.GET)
    public String listProposalReview() {
        return "/module/cpm/proposalReview";
    }


	//
	// Proposer-Reviewer webservice endpoints
	//

    @RequestMapping(value = "/cpm/dictionarymanager/proposals", method = RequestMethod.POST)
    public @ResponseBody SubmissionResponseDto submitProposal(HttpServletRequest request, @RequestBody final SubmissionDto incomingProposal) throws IOException {

		final ProposedConceptService service = Context.getService(ProposedConceptService.class);
		final ProposedConceptResponsePackage proposedConceptResponsePackage = new ProposedConceptResponsePackage();
		proposedConceptResponsePackage.setName(incomingProposal.getName());
		proposedConceptResponsePackage.setEmail(incomingProposal.getEmail());
		proposedConceptResponsePackage.setDescription(incomingProposal.getDescription());
		proposedConceptResponsePackage.setProposedConceptPackageUuid("is-this-really-needed?");

		if (incomingProposal.getConcepts() != null) {
			for (ConceptDto concept : incomingProposal.getConcepts()) {
				ProposedConceptResponse response = new ProposedConceptResponse();
				response.setName(concept.getName());
				response.setDescription(concept.getDescription());
				response.setProposedConceptUuid(concept.getUuid());
				proposedConceptResponsePackage.addProposedConcept(response);
			}
		}

//		String authHeader = request.getHeader("authorization");
//		String encodedValue = authHeader.split(" ")[1];
//		final byte[] bytes = DatatypeConverter.parseBase64Binary(encodedValue);
//		String decodedValue = new String(bytes);
//		final String[] strings = decodedValue.split(":");
//
//		final String username = strings[0];
//		final String password = strings[1];
//		Context.authenticate(username, password);

		service.saveProposedConceptResponsePackage(proposedConceptResponsePackage);

		SubmissionResponseDto responseDto = new SubmissionResponseDto();
        responseDto.setStatus("OK");
        return responseDto;
    }
}
