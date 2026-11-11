package com.yash.grabpic.photo;

public enum PhotoStatus {
    // Photo saved to disk and recorded in DB, waiting for face recognition pipeline
    UPLOADED,

    // Currently being processed by face detection and embedding extraction
    PROCESSING,

    // Faces extracted, embeddings clustered into persons successfully
    DONE,

    // Error during processing (e.g., corrupt file, AI service unreachable)
    FAILED
}
