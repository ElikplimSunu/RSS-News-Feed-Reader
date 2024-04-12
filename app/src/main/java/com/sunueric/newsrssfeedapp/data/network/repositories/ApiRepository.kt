package com.sunueric.newsrssfeedapp.data.network.repositories

import android.util.Log
import android.util.Xml
import com.sunueric.newsrssfeedapp.data.network.models.Item
import org.xmlpull.v1.XmlPullParser

fun parseRssFeed(feed: String): List<Item> {
    val items = mutableListOf<Item>()
    val parser: XmlPullParser = Xml.newPullParser()
    parser.setInput(feed.byteInputStream(), null)

    // Initially, find the <rss> start tag
    while (parser.next() != XmlPullParser.END_DOCUMENT) {
        if (parser.eventType == XmlPullParser.START_TAG && parser.name == "channel") {
            // Once we find <channel>, we start processing items
            break
        }
    }

    // Process all <item> tags within <channel>
    while (parser.next() != XmlPullParser.END_DOCUMENT) {
        if (parser.eventType == XmlPullParser.END_TAG && parser.name == "channel") {
            // If we've reached the end of <channel>, we're done
            break
        }

        if (parser.eventType == XmlPullParser.START_TAG && parser.name == "item") {
            // Found an <item>, process it
            items.add(readItem(parser))
        }
    }

    return items
}

private fun readItem(parser: XmlPullParser): Item {
    // We assume parser is at an <item> start tag
    var title: String? = null
    var link: String? = null
    var description: String? = null
    var imageUrl: String? = null
    var publicationDate: String? = null

    while (!(parser.next() == XmlPullParser.END_TAG && parser.name == "item")) {
        if (parser.eventType != XmlPullParser.START_TAG) {
            continue
        }

        when (parser.name) {
            "title" -> title = readText(parser)
            "link" -> link = readText(parser)
            "description" -> description = readText(parser)
            "pubDate" -> publicationDate = readText(parser)
            "content" -> {
                imageUrl = parser.getAttributeValue(null, "url")
                skip(parser)
            }
        }
    }
    Log.d("NewsFeed", "new thumbnail url: $imageUrl")

    return Item(title ?: "", link ?: "", description ?: "", publicationDate ?: "", imageUrl ?: "")
}

private fun readText(parser: XmlPullParser): String {
    var result = ""
    if (parser.next() == XmlPullParser.TEXT) {
        result = parser.text
        parser.nextTag() // Move to the next tag, which should be the closing tag.
    }
    return result
}

private fun skip(parser: XmlPullParser) {
    if (parser.eventType != XmlPullParser.START_TAG) {
        throw IllegalStateException()
    }
    var depth = 1
    while (depth != 0) {
        when (parser.next()) {
            XmlPullParser.END_TAG -> depth--
            XmlPullParser.START_TAG -> depth++
        }
    }
}