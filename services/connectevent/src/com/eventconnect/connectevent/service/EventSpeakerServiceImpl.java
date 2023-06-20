/*Copyright (c) 2023-2024 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.eventconnect.connectevent.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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

import com.eventconnect.connectevent.EventSpeaker;


/**
 * ServiceImpl object for domain model class EventSpeaker.
 *
 * @see EventSpeaker
 */
@Service("connectevent.EventSpeakerService")
@Validated
@EntityService(entityClass = EventSpeaker.class, serviceId = "connectevent")
public class EventSpeakerServiceImpl implements EventSpeakerService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventSpeakerServiceImpl.class);


    @Autowired
    @Qualifier("connectevent.EventSpeakerDao")
    private WMGenericDao<EventSpeaker, Integer> wmGenericDao;

    @Autowired
    @Qualifier("wmAppObjectMapper")
    private ObjectMapper objectMapper;


    public void setWMGenericDao(WMGenericDao<EventSpeaker, Integer> wmGenericDao) {
        this.wmGenericDao = wmGenericDao;
    }

    @Transactional(value = "connecteventTransactionManager")
    @Override
    public EventSpeaker create(EventSpeaker eventSpeaker) {
        LOGGER.debug("Creating a new EventSpeaker with information: {}", eventSpeaker);

        EventSpeaker eventSpeakerCreated = this.wmGenericDao.create(eventSpeaker);
        // reloading object from database to get database defined & server defined values.
        return this.wmGenericDao.refresh(eventSpeakerCreated);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public EventSpeaker getById(Integer eventspeakerId) {
        LOGGER.debug("Finding EventSpeaker by id: {}", eventspeakerId);
        return this.wmGenericDao.findById(eventspeakerId);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public EventSpeaker findById(Integer eventspeakerId) {
        LOGGER.debug("Finding EventSpeaker by id: {}", eventspeakerId);
        try {
            return this.wmGenericDao.findById(eventspeakerId);
        } catch (EntityNotFoundException ex) {
            LOGGER.debug("No EventSpeaker found with id: {}", eventspeakerId, ex);
            return null;
        }
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public List<EventSpeaker> findByMultipleIds(List<Integer> eventspeakerIds, boolean orderedReturn) {
        LOGGER.debug("Finding EventSpeakers by ids: {}", eventspeakerIds);

        return this.wmGenericDao.findByMultipleIds(eventspeakerIds, orderedReturn);
    }


    @Transactional(rollbackFor = EntityNotFoundException.class, value = "connecteventTransactionManager")
    @Override
    public EventSpeaker update(EventSpeaker eventSpeaker) {
        LOGGER.debug("Updating EventSpeaker with information: {}", eventSpeaker);

        this.wmGenericDao.update(eventSpeaker);
        this.wmGenericDao.refresh(eventSpeaker);

        return eventSpeaker;
    }

    @Transactional(value = "connecteventTransactionManager")
    @Override
    public EventSpeaker partialUpdate(Integer eventspeakerId, Map<String, Object>eventSpeakerPatch) {
        LOGGER.debug("Partially Updating the EventSpeaker with id: {}", eventspeakerId);

        EventSpeaker eventSpeaker = getById(eventspeakerId);

        try {
            ObjectReader eventSpeakerReader = this.objectMapper.reader().forType(EventSpeaker.class).withValueToUpdate(eventSpeaker);
            eventSpeaker = eventSpeakerReader.readValue(this.objectMapper.writeValueAsString(eventSpeakerPatch));
        } catch (IOException ex) {
            LOGGER.debug("There was a problem in applying the patch: {}", eventSpeakerPatch, ex);
            throw new InvalidInputException("Could not apply patch",ex);
        }

        eventSpeaker = update(eventSpeaker);

        return eventSpeaker;
    }

    @Transactional(value = "connecteventTransactionManager")
    @Override
    public EventSpeaker delete(Integer eventspeakerId) {
        LOGGER.debug("Deleting EventSpeaker with id: {}", eventspeakerId);
        EventSpeaker deleted = this.wmGenericDao.findById(eventspeakerId);
        if (deleted == null) {
            LOGGER.debug("No EventSpeaker found with id: {}", eventspeakerId);
            throw new EntityNotFoundException(MessageResource.create("com.wavemaker.runtime.entity.not.found"), EventSpeaker.class.getSimpleName(), eventspeakerId);
        }
        this.wmGenericDao.delete(deleted);
        return deleted;
    }

    @Transactional(value = "connecteventTransactionManager")
    @Override
    public void delete(EventSpeaker eventSpeaker) {
        LOGGER.debug("Deleting EventSpeaker with {}", eventSpeaker);
        this.wmGenericDao.delete(eventSpeaker);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public Page<EventSpeaker> findAll(QueryFilter[] queryFilters, Pageable pageable) {
        LOGGER.debug("Finding all EventSpeakers");
        return this.wmGenericDao.search(queryFilters, pageable);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager")
    @Override
    public Page<EventSpeaker> findAll(String query, Pageable pageable) {
        LOGGER.debug("Finding all EventSpeakers");
        return this.wmGenericDao.searchByQuery(query, pageable);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager", timeout = 300)
    @Override
    public Downloadable export(ExportType exportType, String query, Pageable pageable) {
        LOGGER.debug("exporting data in the service connectevent for table EventSpeaker to {} format", exportType);
        return this.wmGenericDao.export(exportType, query, pageable);
    }

    @Transactional(readOnly = true, value = "connecteventTransactionManager", timeout = 300)
    @Override
    public void export(DataExportOptions options, Pageable pageable, OutputStream outputStream) {
        LOGGER.debug("exporting data in the service connectevent for table EventSpeaker to {} format", options.getExportType());
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



}