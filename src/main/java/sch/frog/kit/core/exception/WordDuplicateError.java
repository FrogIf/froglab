package sch.frog.kit.core.exception;

public class WordDuplicateError extends Error {

    public WordDuplicateError(String word) {
        super("duplicate define word : " + word);
    }
}
