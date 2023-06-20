/*Copyright (c) 2023-2024 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.eventconnect.connectevent.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.wavemaker.commons.InvalidInputException;
import com.wavemaker.commons.MessageResource;
import com.wavemaker.runtime.commons.file.model.Downloadable;
import com.wavemaker.runtime.data.annotations.EntityService;
import com.wavemaker.runtime.data.dao.WMGenericDao;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;

import com.eventconnect.connectevent.Attendee;
import com.eventconnect.connectevent.EventDetail;
import com.eventconnect.connectevent.EventSpeaker;


/**
 * ServiceImpl object for domain model class EventDetail.
 *
 * @see EventDetail
 */
@Service("connectevent.EventDetailService")
@Validated
@EntityService(entityClass = EventDetail.class, serviceId = "connectevent")
public class EventDetailServiceImpl implements EventDetailService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventDetailServiceImpl.class);

    @Lazy
    @Autowired
    @Qualifier("connectevent.EventSpeakerService")
    private EventSpeakerService eventSpeakerService;

    @Lazy
    @Autowired
    @Qualifier("connectevent.AttendeeService")
    private AttendeeService attendeeService;

    @Autowired
    @Qualifier("connectevent.EventDetailDao")
    private WMGenericDao<EventDetail, Integer> wmGenericDao;

    @Autowired
    @Qualifier("wmAppObjectMapper")
    private ObjectMapper objectMapper;


    public void setWMGenericDao(WMGenericDao<EventDetail, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "connecteventTransactionManager")
    @Override
    public EventDetail create(EventDetail eventDetail) {
        LOGGER.debug("Creating a new EventDetail with information: {}", eventDetail);

        EventDetail eventDetailCreated = this.wmGenericDao.create(eventDetail);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(eventDetailCreated);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public EventDetail getById(Integer eventdetailId) {
        LOGGER.debug("Finding EventDetail by id: {}", eventdetailId);
        return this.wmGenericDao.findById(eventdetailId);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public EventDetail findById(Integer eventdetailId) {
        LOGGER.debug("Finding EventDetail by id: {}", eventdetailId);
        try {
            return this.wmGenericDao.findById(eventdetailId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No EventDetail found with id: {}", eventdetailId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public List<EventDetail> findByMultipleIds(List<Integer> eventdetailIds, boolean orderedReturn) {
        LOGGER.debug("Finding EventDetails by ids: {}", eventdetailIds);

        return this.wmGenericDao.findByMultipleIds(eventdetailIds, orderedReturn);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public EventDetail getByTitle(String title) {
        Map<String, Object> titleMap = new HashMap<>();
        titleMap.put("title", title);

        LOGGER.debug("Finding EventDetail by unique keys: {}", titleMap);
        return this.wmGenericDao.findByUniqueKey(titleMap);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public EventDetail getByMeetingLink(String meetingLink) {
        Map<String, Object> meetingLinkMap = new HashMap<>();
        meetingLinkMap.put("meetingLink", meetingLink);

        LOGGER.debug("Finding EventDetail by unique keys: {}", meetingLinkMap);
        return this.wmGenericDao.findByUniqueKey(meetingLinkMap);
    }

    @Transactional(rollbackFor = EntityNotFoundException.class, value = "connecteventTransactionManager")
    @Override
    public EventDetail update(EventDetail eventDetail) {
        LOGGER.debug("Updating EventDetail with information: {}", eventDetail);

        this.wmGenericDao.update(eventDetail);
        this.wmGenericDao.refresh(eventDetail);

        return eventDetail;
    }

    @Transactional(value = "connecteventTransactionManager")
    @Override
    public EventDetail partialUpdate(Integer eventdetailId, Map<String, Object>eventDetailPatch) {
        LOGGER.debug("Partially Updating the EventDetail with id: {}", eventdetailId);

        EventDetail eventDetail = getById(eventdetailId);

        try {
            ObjectReader eventDetailReader = this.objectMapper.reader().forType(EventDetail.class).withValueToUpdate(eventDetail);
            eventDetail = eventDetailReader.readValue(this.objectMapper.writeValueAsString(eventDetailPatch));
        } catch (IOException ex) {
            LOGGER.debug("There was a problem in applying the patch: {}", eventDetailPatch, ex);
            throw new InvalidInputException("Could not apply patch",ex);
        }

        eventDetail = update(eventDetail);

        return eventDetail;
    }

    @Transactional(value = "connecteventTransactionManager")
    @Override
    public EventDetail delete(Integer eventdetailId) {
        LOGGER.debug("Deleting EventDetail with id: {}", eventdetailId);
        EventDetail deleted = this.wmGenericDao.findById(eventdetailId);
        if (deleted == null) {
            LOGGER.debug("No EventDetail found with id: {}", eventdetailId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), EventDetail.class.getSimpleName(), eventdetailId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "connecteventTransactionManager")
    @Override
    public void delete(EventDetail eventDetail) {
        LOGGER.debug("Deleting EventDetail with {}", eventDetail);
        this.wmGenericDao.delete(eventDetail);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public Page<EventDetail> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all EventDetails");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public Page<EventDetail> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all EventDetails");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service connectevent for table EventDetail to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service connectevent for table EventDetail to {} format", options.getExportType());
        this.wmGenericDao.export(options, pageable, outputStream);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public long count(String query) {
        return this.wmGenericDao.count(query);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable) {
        return this.wmGenericDao.getAggregatedValues(aggregationInfo, pageable);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public Page<Attendee> findAssociatedAttendees(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated attendees");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("eventDetail.id = '" + id + "'");

        return attendeeService.findAll(queryBuilder.toString(), pageable);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public Page<EventSpeaker> findAssociatedEventSpeakers(Integer id, Pageable pageable) {
        LOGGER.debug("Fetching all associated eventSpeakers");

        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("eventDetail.id = '" + id + "'");

        return eventSpeakerService.findAll(queryBuilder.toString(), pageable);
    }

    /**
     * This setter method should only be used by unit tests
     *
     * @param service EventSpeakerService instance
     */
    protected void setEventSpeakerService(EventSpeakerService service) {
        this.eventSpeakerService = service;
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