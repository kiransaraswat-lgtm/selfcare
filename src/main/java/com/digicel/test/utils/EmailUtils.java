package com.digicel.test.utils;

import java.util.List;
import java.util.ArrayList;
import java.util.Properties;
import java.io.IOException;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.search.FlagTerm;
import javax.mail.Multipart;
import javax.mail.BodyPart;
import javax.mail.Part;

public class EmailUtils {

    // Connect to Gmail (or any IMAP server)
    public Store connectToGmail(Properties prop) throws Exception {
        Session session = Session.getInstance(prop, null);
        Store store = session.getStore("imaps");
        store.connect(prop.getProperty("emailHost"),
                      prop.getProperty("emailUser"),
                      prop.getProperty("emailPassword"));
        return store;
    }

    // Get unread messages by sender and subject
    public List<String> getUnreadMessageByFromEmail(Store store, String folderName,
                                                   String from, String subject)
            throws MessagingException, IOException {
        List<String> messagesList = new ArrayList<>();

        Folder folder = store.getFolder(folderName != null ? folderName : "INBOX");
        if (!folder.exists()) {
            folder = store.getFolder("INBOX");
        }

        try {
            folder.open(Folder.READ_ONLY);
            Message[] messages = folder.search(new FlagTerm(new javax.mail.Flags(javax.mail.Flags.Flag.SEEN), false));
            for (Message msg : messages) {
                String fromStr = null;
                if (msg.getFrom() != null && msg.getFrom().length > 0) {
                    fromStr = msg.getFrom()[0].toString();
                }
                String subj = msg.getSubject() != null ? msg.getSubject() : "";
                if (fromStr != null && fromStr.contains(from) && subj.contains(subject)) {
                    try {
                        messagesList.add(extractText(msg));
                    } catch (Exception e) {
                        Object content = msg.getContent();
                        messagesList.add(content != null ? content.toString() : "");
                    }
                }
            }
        } finally {
            if (folder.isOpen()) {
                folder.close(false);
            }
        }
        return messagesList;
    }

    private String extractText(Message message) throws Exception {
        if (message.isMimeType("text/*")) {
            Object content = message.getContent();
            return content != null ? content.toString() : "";
        }
        if (message.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) message.getContent();
            return extractTextFromMultipart(mp);
        }
        Object content = message.getContent();
        return content != null ? content.toString() : "";
    }

    private String extractTextFromMultipart(Multipart multipart) throws Exception {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart bp = multipart.getBodyPart(i);
            if (Part.ATTACHMENT.equalsIgnoreCase(bp.getDisposition())) {
                continue;
            }
            if (bp.isMimeType("text/plain") || bp.isMimeType("text/html")) {
                Object c = bp.getContent();
                if (c != null) {
                    sb.append(c.toString()).append('\n');
                }
            } else if (bp.isMimeType("multipart/*")) {
                sb.append(extractTextFromMultipart((Multipart) bp.getContent()));
            }
        }
        return sb.toString().trim();
    }
}
