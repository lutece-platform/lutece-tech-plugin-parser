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

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import fr.paris.lutece.plugins.lucene.service.indexer.IFileIndexer;
import fr.paris.lutece.portal.service.util.AppLogService;

public class ParserFactory implements IParserFactory
{

    private Map<String, IStreamParser> _mapParsers;
    private Map<String, IFileIndexer> _mapIndexers;

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<IStreamParser> getParser( String strMimeType )
    {
        if ( _mapParsers != null )
        {
            return Optional.ofNullable( _mapParsers.get( strMimeType ) );
        }
        return Optional.empty( );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setParsersMap( Map<String, IStreamParser> mapParsers )
    {
        _mapParsers = mapParsers;
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public void setIndexersMap( Map<String, IFileIndexer> mapIndexers )
    {
        _mapIndexers = new HashMap<>( );
        mapIndexers.forEach( ( key, value ) -> {
            if ( value instanceof IStreamParser )
            {
                _mapParsers.put( key, (IStreamParser) value );
            }
            else
            {
                _mapIndexers.put( key, value );
                AppLogService.info( "{}:is note IStreamParser bean", value );
            }

        } );
    }

    /**
     * {@inheritDoc}
     */
    @Deprecated
    @Override
    public IFileIndexer getIndexer( String strMimeType )
    {

        Optional<IStreamParser> fileParser = getParser( strMimeType );
        if ( fileParser.isPresent( ) )
        {
            return fileParser.get( );
        }
        return _mapIndexers.get( strMimeType );
    }

}
