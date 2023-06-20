/*Copyright (c) 2023-2024 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.eventconnect.connectevent.controller;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wavemaker.commons.wrapper.StringWrapper;
import com.wavemaker.runtime.commons.file.manager.ExportedFileManager;
import com.wavemaker.runtime.commons.file.model.Downloadable;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;
import com.wavemaker.tools.api.core.annotations.MapTo;
import com.wavemaker.tools.api.core.annotations.WMAccessVisibility;
import com.wavemaker.tools.api.core.models.AccessSpecifier;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

import com.eventconnect.connectevent.Attendee;
import com.eventconnect.connectevent.service.AttendeeService;


/**
 * Controller object for domain model class Attendee.
 * @see Attendee
 */
@RestController("connectevent.AttendeeController")
@Api(value = "AttendeeController", description = "Exposes APIs to work with Attendee resource.")
@RequestMapping("/connectevent/Attendee")
public class AttendeeController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AttendeeController.class);

    @Autowired
	@Qualifier("connectevent.AttendeeService")
	private AttendeeService attendeeService;

	@Autowired
	private ExportedFileManager exportedFileManager;

	@ApiOperation(value = "Creates a new Attendee instance.")
    @PostMapping
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Attendee createAttendee(@RequestBody Attendee attendee) {
		LOGGER.debug("Create Attendee with information: {}" , attendee);

		attendee = attendeeService.create(attendee);
		LOGGER.debug("Created Attendee with information: {}" , attendee);

	    return attendee;
	}

    @ApiOperation(value = "Returns the Attendee instance associated with the given id.")
    @GetMapping(value = "/{id:.+}")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Attendee getAttendee(@PathVariable("id") Integer id) {
        LOGGER.debug("Getting Attendee with id: {}" , id);

        Attendee foundAttendee = attendeeService.getById(id);
        LOGGER.debug("Attendee details with id: {}" , foundAttendee);

        return foundAttendee;
    }

    @ApiOperation(value = "Updates the Attendee instance associated with the given id.")
    @PutMapping(value = "/{id:.+}")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Attendee editAttendee(@PathVariable("id") Integer id, @RequestBody Attendee attendee) {
        LOGGER.debug("Editing Attendee with id: {}" , attendee.getId());

        attendee.setId(id);
        attendee = attendeeService.update(attendee);
        LOGGER.debug("Attendee details with id: {}" , attendee);

        return attendee;
    }
    
    @ApiOperation(value = "Partially updates the Attendee instance associated with the given id.")
    @PatchMapping(value = "/{id:.+}")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Attendee patchAttendee(@PathVariable("id") Integer id, @RequestBody @MapTo(Attendee.class) Map<String, Object> attendeePatch) {
        LOGGER.debug("Partially updating Attendee with id: {}" , id);

        Attendee attendee = attendeeService.partialUpdate(id, attendeePatch);
        LOGGER.debug("Attendee details after partial update: {}" , attendee);

        return attendee;
    }

    @ApiOperation(value = "Deletes the Attendee instance associated with the given id.")
    @DeleteMapping(value = "/{id:.+}")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public boolean deleteAttendee(@PathVariable("id") Integer id) {
        LOGGER.debug("Deleting Attendee with id: {}" , id);

        Attendee deletedAttendee = attendeeService.delete(id);

        return deletedAttendee != null;
    }

    /**
     * @deprecated Use {@link #findAttendees(String, Pageable)} instead.
     */
    @Deprecated
    @ApiOperation(value = "Returns the list of Attendee instances matching the search criteria.")
    @PostMapping(value = "/search")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Attendee> searchAttendeesByQueryFilters( Pageable pageable, @RequestBody QueryFilter[] queryFilters) {
        LOGGER.debug("Rendering Attendees list by query filter:{}", (Object) queryFilters);
        return attendeeService.findAll(queryFilters, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of Attendee instances matching the optional query (q) request param. If there is no query provided, it returns all the instances. Pagination & Sorting parameters such as page& size, sort can be sent as request parameters. The sort value should be a comma separated list of field names & optional sort order to sort the data on. eg: field1 asc, field2 desc etc ")
    @GetMapping
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Attendee> findAttendees(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Attendees list by filter:", query);
        return attendeeService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns the paginated list of Attendee instances matching the optional query (q) request param. This API should be used only if the query string is too big to fit in GET request with request param. The request has to made in application/x-www-form-urlencoded format.")
    @PostMapping(value="/filter", consumes= "application/x-www-form-urlencoded")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Page<Attendee> filterAttendees(@ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
        LOGGER.debug("Rendering Attendees list by filter", query);
        return attendeeService.findAll(query, pageable);
    }

    @ApiOperation(value = "Returns downloadable file for the data matching the optional query (q) request param.")
    @GetMapping(value = "/export/{exportType}", produces = "application/octet-stream")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public Downloadable exportAttendees(@PathVariable("exportType") ExportType exportType, @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query, Pageable pageable) {
         return attendeeService.export(exportType, query, pageable);
    }

    @ApiOperation(value = "Returns a URL to download a file for the data matching the optional query (q) request param and the required fields provided in the Export Options.") 
    @PostMapping(value = "/export", consumes = "application/json")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
    public StringWrapper exportAttendeesAndGetURL(@RequestBody DataExportOptions exportOptions, Pageable pageable) {
        String exportedFileName = exportOptions.getFileName();
        if(exportedFileName == null || exportedFileName.isEmpty()) {
            exportedFileName = Attendee.class.getSimpleName();
        }
        exportedFileName += exportOptions.getExportType().getExtension();
        String exportedUrl = exportedFileManager.registerAndGetURL(exportedFileName, outputStream -> attendeeService.export(exportOptions, pageable, outputStream));
        return new StringWrapper(exportedUrl);
    }

	@ApiOperation(value = "Returns the total count of Attendee instances matching the optional query (q) request param.")
	@GetMapping(value = "/count")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Long countAttendees( @ApiParam("conditions to filter the results") @RequestParam(value = "q", required = false) String query) {
		LOGGER.debug("counting Attendees");
		return attendeeService.count(query);
	}

    @ApiOperation(value = "Returns aggregated result with given aggregation info")
	@PostMapping(value = "/aggregations")
    @WMAccessVisibility(value = AccessSpecifier.APP_ONLY)
	public Page<Map<String, Object>> getAttendeeAggregatedValues(@RequestBody AggregationInfo aggregationInfo, Pageable pageable) {
        LOGGER.debug("Fetching aggregated results for {}", aggregationInfo);
        return attendeeService.getAggregatedValues(aggregationInfo, pageable);
    }


    /**
	 * This setter method should only be used by unit tests
	 *
	 * @param service AttendeeService instance
	 */
	protected void setAttendeeService(AttendeeService service) {
		this.attendeeService = service;
	}

}