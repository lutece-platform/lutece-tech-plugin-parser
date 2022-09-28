/*
 * Copyright (c) 2002-2022, City of Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.parser.service;

import java.util.Map;
import java.util.Optional;

import fr.paris.lutece.plugins.lucene.service.indexer.IFileIndexer;
import fr.paris.lutece.plugins.lucene.service.indexer.IFileIndexerFactory;

/**
 * Parser Factory
 */
public interface IParserFactory extends IFileIndexerFactory
{
    /**
     * Bean name, to retrieve the factory from the Spring context.
     */
    String BEAN_FILE_PARSER_FACTORY = "parser.parserFactory";

    /**
     * Sets the parsers map
     * 
     * @param mapParsers
     *            the parsers to manage
     */
    void setParsersMap( Map<String, IStreamParser> mapParsers );

    /**
     * Returns the optional parser matching the given MIMETYPE
     * 
     * @param strMimeType
     *            the mime type
     * @return the optional parser
     */
    Optional<IStreamParser> getParser( String strMimeType );

    /**
     * Sets the indexers map
     * 
     * @param mapIndexers
     *            the indexers to manage
     * @deprecated Use {@link #setParsersMap( Map )} this method will be removed in the next version
     */
    @Deprecated
    default void setIndexersMap( Map<String, IFileIndexer> mapIndexers )
    {
        // @see {@link ParserFactory#setIndexersMap(Map<String, IFileIndexer>)}
    }

    /**
     * Returns the indexer matching the given MIMETYPE
     * 
     * @param strMimeType
     *            the mime type
     * @return the index found, <code>null</code> otherwise.
     * @deprecated Use {@link #getParser( String)} this method will be removed in the next version
     */
    @Deprecated
    default IFileIndexer getIndexer( String strMimeType )
    {
        // @see {@link ParserFactory#getIndexer(String)}
        return null;
    }
}
