package eu.anmore.hubs.service;

import java.util.Collection;
import java.util.Date;

public class ServiceCall {

    private ServiceRequest serviceRequest;

    private Date requestDate;

    private ServiceResponse serviceResponse;

    private Date responseDate;

    private Long processingTime;

    private Exception exception;

    private Date exceptionDate;

    private ServiceEndpoint endpoint;

    private Collection<ServiceEndpoint> endpointMetches;
    private String requestUrl;
    private String exceptionMessageDetails;

    private ServiceCall(ServiceRequest serviceRequest, Date requestDate) {
        this.serviceRequest = serviceRequest;
        this.requestDate = requestDate;
    }

    public static ServiceCall ofServiceRequest(ServiceRequest serviceRequest, Date requestDate) {
        return new ServiceCall(serviceRequest, requestDate);
    }

    public void setServiceResponse(ServiceResponse serviceResponse, Date responseDate) {
        this.serviceResponse = serviceResponse;
        this.responseDate = responseDate;
        this.processingTime = responseDate.getTime() - requestDate.getTime();
    }

    public void setException(Exception exception, Date date) {
        this.exception = exception;
        this.exceptionDate = date;
    }

    public ServiceRequest getServiceRequest() {
        return serviceRequest;
    }

    public void setSelectedEndpoint(ServiceEndpoint selectedEndpoint, Collection<ServiceEndpoint> endpoints) {
        this.endpoint = selectedEndpoint;
        this.endpointMetches = endpoints;
    }

    public Long getProcessingTime() {
        return processingTime;
    }

    public String getRequestUrl() {
        return requestUrl;
    }

    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    public String getExceptionMessageDetails() {
        return exceptionMessageDetails;
    }

    public void setExceptionMessageDetails(String exceptionMessageDetails) {
        this.exceptionMessageDetails = exceptionMessageDetails;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("ServiceCall{");
        sb.append("serviceRequest=").append(serviceRequest);
        sb.append(", requestDate=").append(requestDate);
        sb.append(", serviceResponse=").append(serviceResponse);
        sb.append(", responseDate=").append(responseDate);
        sb.append(", processingTime=").append(processingTime);
        sb.append(", exception=").append(exception);
        sb.append(", exceptionDate=").append(exceptionDate);
        sb.append(", endpoint=").append(endpoint);
        sb.append(", endpointMetches=").append(endpointMetches);
        sb.append(", requestUrl='").append(requestUrl).append('\'');
        sb.append(", exceptionMessageDetails='").append(exceptionMessageDetails).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
