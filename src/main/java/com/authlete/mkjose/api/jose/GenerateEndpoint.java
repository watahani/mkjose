/*
 * Copyright (C) 2019 Authlete, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific
 * language governing permissions and limitations under the
 * License.
 */
package com.authlete.mkjose.api.jose;


import java.util.ArrayList;
import java.util.List;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;
import com.authlete.jose.tool.JoseGenerator;


@Path("/api/jose/generate")
public class GenerateEndpoint
{
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public Response post(MultivaluedMap<String, String> params)
    {
        try
        {
            // 200 OK, application/jose
            return Response.ok(buildJose(params), "application/jose").build();
        }
        catch (Exception cause)
        {
            System.err.println(cause.getMessage());

            // 400 Bad Request, text/plain
            return Response
                    .status(Status.BAD_REQUEST)
                    .header("Content-Type", "text/plain")
                    .entity(cause.getMessage())
                    .build();
        }
    }


    private String buildJose(MultivaluedMap<String, String> params) throws Exception
    {
        return new JoseGenerator().execute(buildArgs(params));
    }


    private String[] buildArgs(MultivaluedMap<String, String> params)
    {
        List<String> args = new ArrayList<String>();

        args.add("--sign");

        String[] keys = new String[] {
                "payload", "signing-alg", "jwk-signing-alg", "jws-header"
        };

        for (String key : keys)
        {
            addArg(args, params, key);
        }

        return args.toArray(new String[args.size()]);
    }


    private void addArg(List<String> args, MultivaluedMap<String, String> params, String key)
    {
        String value = params.getFirst(key);

        if (value == null || value.length() == 0)
        {
            return;
        }

        args.add("--" + key);
        args.add(value);
    }
}
