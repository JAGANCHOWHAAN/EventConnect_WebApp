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

import com.eventconnect.connectevent.Attendee;

/**
 * Service object for domain model class {@link Attendee}.
 */
public interface AttendeeService {

    /**
     * Creates a new Attendee. It does cascade insert for all the children in a single transaction.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Attendee if any.
     *
     * @param attendee Details of the Attendee to be created; value cannot be null.
     * @return The newly created Attendee.
     */
    Attendee create(@Valid Attendee attendee);


	/**
     * Returns Attendee by given id if exists.
     *
     * @param attendeeId The id of the Attendee to get; value cannot be null.
     * @return Attendee associated with the given attendeeId.
	 * @throws EntityNotFoundException If no Attendee is found.
     */
    Attendee getById(Integer attendeeId);

    /**
     * Find and return the Attendee by given id if exists, returns null otherwise.
     *
     * @param attendeeId The id of the Attendee to get; value cannot be null.
     * @return Attendee associated with the given attendeeId.
     */
    Attendee findById(Integer attendeeId);

	/**
     * Find and return the list of Attendees by given id's.
     *
     * If orderedReturn true, the return List is ordered and positional relative to the incoming ids.
     *
     * In case of unknown entities:
     *
     * If enabled, A null is inserted into the List at the proper position(s).
     * If disabled, the nulls are not put into the return List.
     *
     * @param attendeeIds The id's of the Attendee to get; value cannot be null.
     * @param orderedReturn Should the return List be ordered and positional in relation to the incoming ids?
     * @return Attendees associated with the given attendeeIds.
     */
    List<Attendee> findByMultipleIds(List<Integer> attendeeIds, boolean orderedReturn);


    /**
     * Updates the details of an existing Attendee. It replaces all fields of the existing Attendee with the given attendee.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Attendee if any.
     *
     * @param attendee The details of the Attendee to be updated; value cannot be null.
     * @return The updated Attendee.
     * @throws EntityNotFoundException if no Attendee is found with given input.
     */
    Attendee update(@Valid Attendee attendee);


    /**
     * Partially updates the details of an existing Attendee. It updates only the
     * fields of the existing Attendee which are passed in the attendeePatch.
     *
     * This method overrides the input field values using Server side or database managed properties defined on Attendee if any.
     *
     * @param attendeeId The id of the Attendee to be deleted; value cannot be null.
     * @param attendeePatch The partial data of Attendee which is supposed to be updated; value cannot be null.
     * @return The updated Attendee.
     * @throws EntityNotFoundException if no Attendee is found with given input.
     */
    Attendee partialUpdate(Integer attendeeId, Map<String, Object> attendeePatch);

    /**
     * Deletes an existing Attendee with the given id.
     *
     * @param attendeeId The id of the Attendee to be deleted; value cannot be null.
     * @return The deleted Attendee.
     * @throws EntityNotFoundException if no Attendee found with the given id.
     */
    Attendee delete(Integer attendeeId);

    /**
     * Deletes an existing Attendee with the given object.
     *
     * @param attendee The instance of the Attendee to be deleted; value cannot be null.
     */
    void delete(Attendee attendee);

    /**
     * Find all Attendees matching the given QueryFilter(s).
     * All the QueryFilter(s) are ANDed to filter the results.
     * This method returns Paginated results.
     *
     * @deprecated Use {@link #findAll(String, Pageable)} instead.
     *
     * @param queryFilters Array of queryFilters to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of matching Attendees.
     *
     * @see QueryFilter
     * @see Pageable
     * @see Page
     */
    @Deprecated
    Page<Attendee> findAll(QueryFilter[] queryFilters, Pageable pageable);

    /**
     * Find all Attendees matching the given input query. This method returns Paginated results.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param query The query to filter the results; No filters applied if the input is null/empty.
     * @param pageable Details of the pagination information along with the sorting options. If null returns all matching records.
     * @return Paginated list of matching Attendees.
     *
     * @see Pageable
     * @see Page
     */
    Page<Attendee> findAll(String query, Pageable pageable);

    /**
     * Exports all Attendees matching the given input query to the given exportType format.
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
     * Exports all Attendees matching the given input query to the given exportType format.
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
     * Retrieve the count of the Attendees in the repository with matching query.
     * Note: Go through the documentation for <u>query</u> syntax.
     *
     * @param query query to filter results. No filters applied if the input is null/empty.
     * @return The count of the Attendee.
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


}