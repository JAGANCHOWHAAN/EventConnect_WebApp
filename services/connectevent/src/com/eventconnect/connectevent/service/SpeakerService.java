/*Copyright (c) 2023-2024 wavemaker.com All Rights Reserved.
 This software is the confidential and proprietary information of wavemaker.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with wavemaker.com*/
package com.eventconnect.connectevent.service;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/

import java.io.OutputStream;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.wavemaker.runtime.commons.file.model.Downloadable;
import com.wavemaker.runtime.data.exception.EntityNotFoundException;
import com.wavemaker.runtime.data.export.DataExportOptions;
import com.wavemaker.runtime.data.export.ExportType;
import com.wavemaker.runtime.data.expression.QueryFilter;
import com.wavemaker.runtime.data.model.AggregationInfo;

import com.eventconnect.connectevent.EventSpeaker;
import com.eventconnect.connectevent.Speaker;

/**
 * Service object for domain model class {@link Speaker}.
 */
public interface SpeakerService {

    /**
     * Creates a new Speaker. It does cascade insert for all the children in a single transaction.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Speaker if any.
     *
     * @param speaker Details of the Speaker to be created; value cannot be null.
     * @return The newly created Speaker.
     */
    Speaker create(@Valid Speaker speaker);


	/**
     * Returns Speaker by given id if exists.
     *
     * @param speakerId The id of the Speaker to get; value cannot be null.
     * @return Speaker associated with the given speakerId.
	 * @throws EntityNotFoundException If no Speaker is found.
     */
    Speaker getById(Integer speakerId);

    /**
     * Find and return the Speaker by given id if exists, returns null otherwise.
     *
     * @param speakerId The id of the Speaker to get; value cannot be null.
     * @return Speaker associated with the given speakerId.
     */
    Speaker findById(Integer speakerId);

	/**
     * Find and return the list of Speakers by given id's.
     *
     * If orderedReturn true, the return List is ordered and positional relative to the incoming ids.
     *
     * In case of unknown entities:
     *
     * If enabled, A null is inserted into the List at the proper position(s).
     * If disabled, the nulls are not put into the return List.
     *
     * @param speakerIds The id's of the Speaker to get; value cannot be null.
     * @param orderedReturn Should the return List be ordered and positional in relation to the incoming ids?
     * @return Speakers associated with the given speakerIds.
     */
    List<Speaker> findByMultipleIds(List<Integer> speakerIds, boolean orderedReturn);


    /**
     * Updates the details of an existing Speaker. It replaces all fields of the existing Speaker with the given speaker.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Speaker if any.
     *
     * @param speaker The details of the Speaker to be updated; value cannot be null.
     * @return The updated Speaker.
     * @throws EntityNotFoundException if no Speaker is found with given input.
     */
    Speaker update(@Valid Speaker speaker);


    /**
     * Partially updates the details of an existing Speaker. It updates only the
     * fields of the existing Speaker which are passed in the speakerPatch.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Speaker if any.
     *
     * @param speakerId The id of the Speaker to be deleted; value cannot be null.
     * @param speakerPatch The partial data of Speaker which is supposed to be updated; value cannot be null.
     * @return The updated Speaker.
     * @throws EntityNotFoundException if no Speaker is found with given input.
     */
    Speaker partialUpdate(Integer speakerId, Map<String, Object> speakerPatch);

    /**
     * Deletes an existing Speaker with the given id.
     *
     * @param speakerId The id of the Speaker to be deleted; value cannot be null.
     * @return The deleted Speaker.
     * @throws EntityNotFoundException if no Speaker found with the given id.
     */
    Speaker delete(Integer speakerId);

    /**
     * Deletes an existing Speaker with the given object.
     *
     * @param speaker The instance of the Speaker to be deleted; value cannot be null.
     */
    void delete(Speaker speaker);

    /**
     * Find all Speakers matching the given QueryFilter(s).
     * All the QueryFilter(s) are ANDed to filter the results.
     * This method returns Paginated results.
     *
     * @deprecated Use {@link #findAll(String, Pageable)} instead.
     *
     * @param queryFilters Array of queryFilters to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of matching Speakers.
     *
     * @see QueryFilter
     * @see Pageable
     * @see Page
     */
    @Deprecated
    Page<Speaker> findAll(QueryFilter[] queryFilters, Pageable pageable);

    /**
     * Find all Speakers matching the given input query. This method returns Paginated results.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param query The query to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of matching Speakers.
     *
     * @see Pageable
     * @see Page
     */
    Page<Speaker> findAll(String query, Pageable pageable);

    /**
     * Exports all Speakers matching the given input query to the given exportType format.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param exportType The format in which to export the data; value cannot be null.
     * @param query The query to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @return The Downloadable file in given export type.
     *
     * @see Pageable
     * @see ExportType
     * @see Downloadable
     */
    Downloadable export(ExportType exportType, String query, Pageable pageable);

    /**
     * Exports all Speakers matching the given input query to the given exportType format.
     *
     * @param options The export options provided by the user; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @param outputStream The output stream of the file for the exported data to be written to.
     *
     * @see DataExportOptions
     * @see Pageable
     * @see OutputStream
     */
    void export(DataExportOptions options, Pageable pageable, OutputStream outputStream);

    /**
     * Retrieve the count of the Speakers in the repository with matching query.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param query query to filter results. No filters applied if the input is null/empty.
     * @return The count of the Speaker.
     */
    long count(String query);

    /**
     * Retrieve aggregated values with matching aggregation info.
     *
     * @param aggregationInfo info related to aggregations.
     * @param pageable Details of the pagination information along with the sorting options. If null exports all matching records.
     * @return Paginated data with included fields.
     *
     * @see AggregationInfo
     * @see Pageable
     * @see Page
	 */
    Page<Map<String, Object>> getAggregatedValues(AggregationInfo aggregationInfo, Pageable pageable);

    /*
     * Returns the associated eventSpeakers for given Speaker id.
     *
     * @param id value of id; value cannot be null
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of associated EventSpeaker instances.
     *
     * @see Pageable
     * @see Page
     */
    Page<EventSpeaker> findAssociatedEventSpeakers(Integer id, Pageable pageable);

}