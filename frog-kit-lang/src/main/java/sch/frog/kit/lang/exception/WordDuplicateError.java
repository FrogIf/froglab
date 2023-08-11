package sch.frog.kit.lang.exception;

public class WordDuplicateError extends Error {

    public WordDuplicateError(String word) {
        super("duplicate define word : " + word);
    }
}
